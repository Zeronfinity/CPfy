package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.END_TIME
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.START_TIME
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.FragmentContestListBinding
import com.zeronfinity.cpfy.view.adapter.AdapterContestList
import com.zeronfinity.cpfy.viewmodel.ClipboardViewModel
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import com.zeronfinity.cpfy.viewmodel.FiltersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

@AndroidEntryPoint
class ContestListFragment : BaseFragment() {
    private var _binding: FragmentContestListBinding? = null
    private val binding get() = _binding!!

    private lateinit var clipboardViewModel: ClipboardViewModel
    private lateinit var contestListViewModel: ContestListViewModel
    private lateinit var filtersViewModel: FiltersViewModel

    @Inject
    lateinit var adapterContestList: AdapterContestList

    private var isFirstTime = true

    private var prevStartTimeLowerBound: Date? = null
    private var prevStartTimeUpperBound: Date? = null
    private var prevEndTimeLowerBound: Date? = null
    private var prevEndTimeUpperBound: Date? = null
    private var prevDurationLowerBound: Int? = null
    private var prevDurationUpperBound: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("onCreateView started")

        _binding = FragmentContestListBinding.inflate(inflater, container, false)

        binding.rvContestList.adapter = adapterContestList
        binding.rvContestList.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvContestList.setHasFixedSize(true)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        logD("onSaveInstanceState() started")

        outState.putBoolean("IS_FIRST_TIME", isFirstTime)
        prevStartTimeLowerBound?.let { outState.putLong("START_TIME_LOWER_BOUND", it.time) }
        prevStartTimeUpperBound?.let { outState.putLong("START_TIME_UPPER_BOUND", it.time) }
        prevEndTimeLowerBound?.let { outState.putLong("END_TIME_LOWER_BOUND", it.time) }
        prevEndTimeUpperBound?.let { outState.putLong("END_TIME_UPPER_BOUND", it.time) }
        prevDurationLowerBound?.let { outState.putInt("DURATION_LOWER_BOUND", it) }
        prevDurationUpperBound?.let { outState.putInt("DURATION_UPPER_BOUND", it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated() started -> isFirstTime: [$isFirstTime]")

        clipboardViewModel = ViewModelProvider(requireActivity()).get(ClipboardViewModel::class.java)

        contestListViewModel = ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeContestListViewModel()

        filtersViewModel = ViewModelProvider(requireActivity()).get(FiltersViewModel::class.java)

        savedInstanceState?.let {
            isFirstTime = it.getBoolean("IS_FIRST_TIME")
            prevStartTimeLowerBound = Date(it.getLong("START_TIME_LOWER_BOUND"))
            prevStartTimeUpperBound = Date(it.getLong("START_TIME_UPPER_BOUND"))
            prevEndTimeLowerBound = Date(it.getLong("END_TIME_LOWER_BOUND"))
            prevEndTimeUpperBound = Date(it.getLong("END_TIME_UPPER_BOUND"))
            prevDurationLowerBound = it.getInt("DURATION_LOWER_BOUND")
            prevDurationUpperBound = it.getInt("DURATION_UPPER_BOUND")
        }

        binding.swipeRefresh.setOnRefreshListener {
            contestListViewModel.fetchContestList()
        }

        if (isFirstTime || isTimeFilterChanged()) {
            contestListViewModel.fetchContestList()
        }

        if (isFirstTime) {
            contestListViewModel.fetchPlatformList()
        }

        if (isFirstTime && !filtersViewModel.isSaved()) {
            filtersViewModel.resetAllFilters()
            filtersViewModel.setSaved(true)
        }

        adapterContestList.clearAllSelected()

        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            logD("addOnDestinationChangedListener -> destination: [$destination]")
            if (destination.id == R.id.clipboardFragment) {
                clipboardViewModel.setSelectedContests(adapterContestList.getSelectedContests())
            }
        }

        setPrevTimesToCurrentValues()
        isFirstTime = false
    }

    private fun observeContestListViewModel() {

        contestListViewModel.errorToastIncomingLiveDataEv.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                logD("errorToastIncomingLiveDataEv: creating error toast")
                activity?.let { fragmentActivity ->
                    Toast.makeText(fragmentActivity.applicationContext, it, Toast.LENGTH_SHORT)
                        .show()
                }
                binding.swipeRefresh.isRefreshing = false
            }
        })

        contestListViewModel.contestListLiveData.observe(viewLifecycleOwner, {
            logD("contestListLiveData -> contestList:[$it]")
            adapterContestList.refreshContestList(it)
            binding.swipeRefresh.isRefreshing = false
        })

        contestListViewModel.platformListLiveData.observe(viewLifecycleOwner, {
            logD("platformListLiveData -> platformList:[$it]")
            contestListViewModel.refreshContestList()
        })

        contestListViewModel.clistWebViewLiveDataEv.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                logD("clistWebViewLiveDataObserver: navigating to web view fragment")
                val action =
                    ContestListFragmentDirections.actionContestListFragmentToWebViewFragment(
                        getString(R.string.clist_login_url)
                    )
                findNavController().safeNavigate(action)
            }
        })
    }

    private fun isTimeFilterChanged(): Boolean {
        var isChanged = false

        if (filtersViewModel.isLowerBoundToday(START_TIME)) {
            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            prevStartTimeLowerBound?.let {
                if (it.compareTo(calendar.time) != 0) {
                    logD("isTimeFilterChanged() -> prevStartTimeLowerBound: [$it], calendar.time: [${calendar.time}]")
                    isChanged = true
                }
            }

            calendar.add(Calendar.DAY_OF_YEAR, filtersViewModel.getDaysAfterToday(START_TIME))

            prevStartTimeUpperBound?.let {
                if (it.compareTo(calendar.time) != 0) {
                    logD("isTimeFilterChanged() -> prevStartTimeUpperBound: [$it], calendar.time: [${calendar.time}]")
                    isChanged = true
                }
            }
        } else {
            val startTimeLowerBound = filtersViewModel.getTimeFilters(START_TIME_LOWER_BOUND)
            prevStartTimeLowerBound?.let {
                if (it.compareTo(startTimeLowerBound) != 0) {
                    logD("isTimeFilterChanged() -> prevStartTimeLowerBound: [$it], startTimeLowerBound: [${startTimeLowerBound}]")
                    isChanged = true
                }
            }

            val startTimeUpperBound = filtersViewModel.getTimeFilters(START_TIME_UPPER_BOUND)
            prevStartTimeUpperBound?.let {
                if (it.compareTo(startTimeUpperBound) != 0) {
                    logD("isTimeFilterChanged() -> prevStartTimeUpperBound: [$it], startTimeUpperBound: [${startTimeUpperBound}]")
                    isChanged = true
                }
            }
        }

        if (filtersViewModel.isLowerBoundToday(END_TIME)) {
            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            prevEndTimeLowerBound?.let {
                if (it.compareTo(calendar.time) != 0) {
                    logD("isTimeFilterChanged() -> prevEndTimeLowerBound: [$it], calendar.time: [${calendar.time}]")
                    isChanged = true
                }
            }

            calendar.add(Calendar.DAY_OF_YEAR, filtersViewModel.getDaysAfterToday(END_TIME))

            prevEndTimeUpperBound?.let {
                if (it.compareTo(calendar.time) != 0) {
                    logD("isTimeFilterChanged() -> prevEndTimeUpperBound: [$it], calendar.time: [${calendar.time}]")
                    isChanged = true
                }
            }
        } else {
            val endTimeLowerBound = filtersViewModel.getTimeFilters(END_TIME_LOWER_BOUND)
            prevEndTimeLowerBound?.let {
                if (it.compareTo(endTimeLowerBound) != 0) {
                    logD("isTimeFilterChanged() -> prevEndTimeLowerBound: [$it], endTimeLowerBound: [${endTimeLowerBound}]")
                    isChanged = true
                }
            }

            val endTimeUpperBound = filtersViewModel.getTimeFilters(END_TIME_UPPER_BOUND)
            prevEndTimeUpperBound?.let {
                if (it.compareTo(endTimeUpperBound) != 0) {
                    logD("isTimeFilterChanged() -> prevEndTimeUpperBound: [$it], endTimeUpperBound: [${endTimeUpperBound}]")
                    isChanged = true
                }
            }
        }

        val durationLowerBound = filtersViewModel.getDurationFilters(DURATION_LOWER_BOUND)
        prevDurationLowerBound?.let {
            if (it.compareTo(durationLowerBound) != 0) {
                logD("isTimeFilterChanged() -> prevDurationLowerBound: [$it], durationLowerBound: [${durationLowerBound}]")
                isChanged = true
            }
        }

        val durationUpperBound = filtersViewModel.getDurationFilters(DURATION_UPPER_BOUND)
        prevDurationUpperBound?.let {
            if (it.compareTo(durationUpperBound) != 0) {
                logD("isTimeFilterChanged() -> prevDurationUpperBound: [$it], durationUpperBound: [${durationUpperBound}]")
                isChanged = true
            }
        }

        logD("isTimeFilterChanged() -> isChanged: [$isChanged]")

        return isChanged
    }

    private fun setPrevTimesToCurrentValues() {
        if (filtersViewModel.isLowerBoundToday(START_TIME)) {
            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            prevStartTimeLowerBound = calendar.time

            calendar.add(Calendar.DAY_OF_YEAR, filtersViewModel.getDaysAfterToday(START_TIME))
            prevStartTimeUpperBound = calendar.time
        } else {
            prevStartTimeLowerBound = filtersViewModel.getTimeFilters(START_TIME_LOWER_BOUND)
            prevStartTimeUpperBound = filtersViewModel.getTimeFilters(START_TIME_UPPER_BOUND)
        }

        if (filtersViewModel.isLowerBoundToday(END_TIME)) {
            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            prevEndTimeLowerBound = calendar.time

            calendar.add(Calendar.DAY_OF_YEAR, filtersViewModel.getDaysAfterToday(END_TIME))
            prevEndTimeUpperBound = calendar.time
        } else {
            prevEndTimeLowerBound = filtersViewModel.getTimeFilters(END_TIME_LOWER_BOUND)
            prevEndTimeUpperBound = filtersViewModel.getTimeFilters(END_TIME_UPPER_BOUND)
        }

        prevDurationLowerBound = filtersViewModel.getDurationFilters(DURATION_LOWER_BOUND)
        prevDurationUpperBound = filtersViewModel.getDurationFilters(DURATION_UPPER_BOUND)
    }
}
