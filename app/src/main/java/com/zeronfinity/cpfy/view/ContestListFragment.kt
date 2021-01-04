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
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.common.FILTER_DATE_TIME_FORMAT
import com.zeronfinity.cpfy.databinding.FragmentContestListBinding
import com.zeronfinity.cpfy.view.adapter.AdapterContestList
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import com.zeronfinity.cpfy.viewmodel.FiltersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ContestListFragment : BaseFragment() {
    private var _binding: FragmentContestListBinding? = null
    private val binding get() = _binding!!

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

        if (isFirstTime) {
            contestListViewModel.fetchContestList()
            contestListViewModel.fetchPlatformList()
        } else if (isTimeFilterChanged()) {
            contestListViewModel.fetchContestList()
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
            }
        })

        contestListViewModel.contestListLiveData.observe(viewLifecycleOwner, {
            logD("contestListLiveData -> contestList:[$it]")
            adapterContestList.refreshContestList(it)
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

        val startTimeLowerBound = filtersViewModel.getTimeFilters(START_TIME_LOWER_BOUND)
        prevStartTimeLowerBound?.let {
            if (prevStartTimeLowerBound != startTimeLowerBound) {
                isChanged = true
            }
        }

        val startTimeUpperBound = filtersViewModel.getTimeFilters(START_TIME_UPPER_BOUND)
        prevStartTimeUpperBound?.let {
            if (prevStartTimeUpperBound != startTimeUpperBound) {
                isChanged = true
            }
        }

        val endTimeLowerBound = filtersViewModel.getTimeFilters(END_TIME_LOWER_BOUND)
        prevEndTimeLowerBound?.let {
            if (prevEndTimeLowerBound != endTimeLowerBound) {
                isChanged = true
            }
        }

        val endTimeUpperBound = filtersViewModel.getTimeFilters(END_TIME_UPPER_BOUND)
        prevEndTimeUpperBound?.let {
            if (prevEndTimeUpperBound != endTimeUpperBound) {
                isChanged = true
            }
        }

        val durationLowerBound = filtersViewModel.getDurationFilters(DURATION_LOWER_BOUND)
        prevDurationLowerBound?.let {
            if (prevDurationLowerBound != durationLowerBound) {
                isChanged = true
            }
        }

        val durationUpperBound = filtersViewModel.getDurationFilters(DURATION_UPPER_BOUND)
        prevDurationUpperBound?.let {
            if (prevDurationUpperBound != durationUpperBound) {
                isChanged = true
            }
        }

        logD("isTimeFilterChanged() -> isChanged: [$isChanged]")

        return isChanged
    }

    private fun setPrevTimesToCurrentValues() {
        prevStartTimeLowerBound = filtersViewModel.getTimeFilters(START_TIME_LOWER_BOUND)
        prevStartTimeUpperBound = filtersViewModel.getTimeFilters(START_TIME_UPPER_BOUND)
        prevEndTimeLowerBound = filtersViewModel.getTimeFilters(END_TIME_LOWER_BOUND)
        prevEndTimeUpperBound = filtersViewModel.getTimeFilters(END_TIME_UPPER_BOUND)
        prevDurationLowerBound = filtersViewModel.getDurationFilters(DURATION_LOWER_BOUND)
        prevDurationUpperBound = filtersViewModel.getDurationFilters(DURATION_UPPER_BOUND)
    }
}
