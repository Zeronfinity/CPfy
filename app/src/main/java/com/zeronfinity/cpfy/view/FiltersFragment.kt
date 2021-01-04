package com.zeronfinity.cpfy.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.logger.logE
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.common.makeDurationText
import com.zeronfinity.cpfy.databinding.FragmentFiltersBinding
import com.zeronfinity.cpfy.view.adapter.AdapterPlatformFilters
import com.zeronfinity.cpfy.view.adapter.AdapterPlatformFilters.PlatformFilterClickListener
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import com.zeronfinity.cpfy.viewmodel.FiltersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class FiltersFragment
    : BaseFragment(), PlatformFilterClickListener {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!

    private lateinit var filtersViewModel: FiltersViewModel
    private lateinit var contentListViewModel: ContestListViewModel

    @Inject
    lateinit var adapterPlatformFilters: AdapterPlatformFilters

    private val simpleDateFormat = SimpleDateFormat(
        "dd-MM-yy\nhh:mm a",
        Locale.getDefault()
    )

    private var isContestListFetchRequired: Boolean = false
    private var isContestListRefreshRequired: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("onCreateView() started")
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStop() {
        logD("onStop() started -> isContestListRefreshRequired: [$isContestListRefreshRequired]")
        if (isContestListFetchRequired) {
            contentListViewModel.fetchContestList()
        }
        if (isContestListRefreshRequired) {
            contentListViewModel.refreshContestList()
        }
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated() started")

        binding.rvPlatforms.adapter = adapterPlatformFilters
        binding.rvPlatforms.layoutManager = GridLayoutManager(activity, 3)
        binding.rvPlatforms.setHasFixedSize(true)

        contractRecyclerView()
        binding.ivExpand.setOnClickListener {
            when (it.tag) {
                "contracted" -> {
                    expandRecyclerView()
                }
                "expanded" -> {
                    contractRecyclerView()
                }
                else -> {
                    logE("Invalid ivExpand tag: " + it.tag.toString())
                }
            }
        }

        adapterPlatformFilters.setPlatformFilterClickListener(this)

        contentListViewModel = ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeContestListViewModel()

        filtersViewModel = ViewModelProvider(requireActivity()).get(FiltersViewModel::class.java)
        observeFilterDialogViewModel()

        filtersViewModel.loadTimeBasedFilterButtonTexts()

        setUpButtons()
    }

    private fun contractRecyclerView() {
        binding.ivExpand.tag = "contracted"
        binding.ivExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
        adapterPlatformFilters.setMaxItemCount()
    }

    private fun expandRecyclerView() {
        binding.ivExpand.tag = "expanded"
        binding.ivExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
        adapterPlatformFilters.resetMaxItemCount()
    }

    private fun observeContestListViewModel() {
        contentListViewModel.platformListLiveData.observe(viewLifecycleOwner, {
            logD("platformListLiveData -> platformList[$it]")
            adapterPlatformFilters.refreshPlatformList(it)
        })
    }

    private fun observeFilterDialogViewModel() {
        filtersViewModel.startTimeLowerBoundLiveData.observe(viewLifecycleOwner, {
            binding.btnStartTimeLowerBound.text = it
        })

        filtersViewModel.startTimeUpperBoundLiveData.observe(viewLifecycleOwner, {
            binding.btnStartTimeUpperBound.text = it
        })

        filtersViewModel.endTimeLowerBoundLiveData.observe(viewLifecycleOwner, {
            binding.btnEndTimeLowerBound.text = it
        })

        filtersViewModel.endTimeUpperBoundLiveData.observe(viewLifecycleOwner, {
            binding.btnEndTimeUpperBound.text = it
        })

        filtersViewModel.durationLowerBoundLiveData.observe(viewLifecycleOwner, {
            binding.btnDurationLowerBound.text = makeDurationText(it)
        })

        filtersViewModel.durationUpperBoundLiveData.observe(viewLifecycleOwner, {
            binding.btnDurationUpperBound.text = makeDurationText(it)
        })
    }

    override fun onPlatformFilterClick() {
        isContestListRefreshRequired = true
    }

    private fun setUpButtons() {
        binding.btnStartTimeLowerBound.setOnClickListener { view ->
            val btn = view as Button
            val date = simpleDateFormat.parse(btn.text as String)
            date?.let {
                showDatePicker(START_TIME_LOWER_BOUND, it)
            }
        }

        binding.btnStartTimeUpperBound.setOnClickListener { view ->
            val btn = view as Button
            val date = simpleDateFormat.parse(btn.text as String)
            date?.let {
                showDatePicker(START_TIME_UPPER_BOUND, it)
            }
        }

        binding.btnEndTimeLowerBound.setOnClickListener { view ->
            val btn = view as Button
            val date = simpleDateFormat.parse(btn.text as String)
            date?.let {
                showDatePicker(END_TIME_LOWER_BOUND, it)
            }
        }

        binding.btnEndTimeUpperBound.setOnClickListener { view ->
            val btn = view as Button
            val date = simpleDateFormat.parse(btn.text as String)
            date?.let {
                showDatePicker(END_TIME_UPPER_BOUND, it)
            }
        }

        binding.btnDurationLowerBound.setOnClickListener {
            showDurationPicker(DURATION_LOWER_BOUND)
        }

        binding.btnDurationUpperBound.setOnClickListener {
            showDurationPicker(DURATION_UPPER_BOUND)
        }

        binding.btnAllPlatforms.tag = 0
        binding.btnAllPlatforms.text = getString(R.string.enable_all_platforms)
        binding.btnAllPlatforms.setOnClickListener {
            val btn = it as Button
            when (btn.tag) {
                0 -> {
                    filtersViewModel.enableAllPlatforms()
                    btn.text = getString(R.string.disable_all_platforms)
                }
                1 -> {
                    filtersViewModel.disableAllPlatforms()
                    btn.text = getString(R.string.enable_all_platforms)
                }
            }
            btn.tag = 1 - (btn.tag as Int)
        }

        binding.btnReset.setOnClickListener {
            filtersViewModel.resetAllFilters()
        }
    }

    private fun showDatePicker(filterTimeEnum: FilterTimeEnum, date: Date) {
        val calendar = GregorianCalendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        context?.let {
            DatePickerDialog(
                it,
                { _: DatePicker, year: Int, month: Int, day: Int ->
                     showTimePicker(filterTimeEnum, date, GregorianCalendar(year, month, day))
                },
                year,
                month,
                day
            ).show()
        }
    }

    private fun showTimePicker(filterTimeEnum: FilterTimeEnum, prevDate: Date, calendar: GregorianCalendar) {
        val prevCalendar = GregorianCalendar.getInstance()
        prevCalendar.time = prevDate

        val hour = prevCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = prevCalendar.get(Calendar.MINUTE)
        context?.let {
            TimePickerDialog(
                it,
                { _: TimePicker, hour: Int, minute: Int ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    if (prevCalendar.time != calendar.time) {
                        isContestListFetchRequired = true
                    }
                    filtersViewModel.setTimeFilters(filterTimeEnum, calendar.time)
                },
                hour,
                minute,
                false
            ).show()
        }
    }

    private fun showDurationPicker(filterDurationEnum: FilterDurationEnum) {
        val action = FiltersFragmentDirections.actionFiltersFragmentToDurationPickerDiaFrag(
            filterDurationEnum
        )
        findNavController().safeNavigate(action)
    }
}
