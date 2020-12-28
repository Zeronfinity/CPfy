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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.cpfy.R
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
    : Fragment(), PlatformFilterClickListener {
    private val LOG_TAG = FiltersFragment::class.simpleName

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlatforms.adapter = adapterPlatformFilters
        binding.rvPlatforms.layoutManager = GridLayoutManager(activity, 3)
        binding.rvPlatforms.setHasFixedSize(true)

        adapterPlatformFilters.setPlatformFilterClickListener(this)

        contentListViewModel = ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeContestListViewModel()

        filtersViewModel = ViewModelProvider(requireActivity()).get(FiltersViewModel::class.java)
        observeFilterDialogViewModel()

        filtersViewModel.loadTimeBasedFilterButtonTexts()

        setUpButtons()
    }

    private fun observeContestListViewModel() {
        contentListViewModel.contestListUpdatedLiveData.observe(viewLifecycleOwner, Observer {
            refreshPlatformList()
        })
    }

    private fun observeFilterDialogViewModel() {
        filtersViewModel.startTimeLowerBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnStartTimeLowerBound.text = it
        })

        filtersViewModel.startTimeUpperBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnStartTimeUpperBound.text = it
        })

        filtersViewModel.endTimeLowerBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnEndTimeLowerBound.text = it
        })

        filtersViewModel.endTimeUpperBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnEndTimeUpperBound.text = it
        })

        filtersViewModel.durationLowerBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnDurationLowerBound.text = makeDurationText(it)
        })

        filtersViewModel.durationUpperBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnDurationUpperBound.text = makeDurationText(it)
        })

        filtersViewModel.platformListUpdatedLiveData.observe(viewLifecycleOwner, {
            refreshPlatformList()
        })
    }

    private fun refreshPlatformList() {
        adapterPlatformFilters.refreshPlatformList()
    }

    override fun onPlatformFilterClick() {
        contentListViewModel.updateContestList()
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
        findNavController().navigate(action)
    }

    private fun makeDurationText(duration: Int): String {
        var remainingDuration = duration / 60

        val minutes = remainingDuration % 60
        remainingDuration /= 60

        val hours = remainingDuration % 24
        remainingDuration /= 24

        val days = remainingDuration

        return "${days}d ${hours}h ${minutes}m"
    }
}
