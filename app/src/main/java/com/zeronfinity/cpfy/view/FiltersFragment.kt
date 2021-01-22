package com.zeronfinity.cpfy.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.logger.logE
import com.zeronfinity.core.repository.FilterTimeRangeRepository.*
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.END_TIME
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.START_TIME
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.common.Either
import com.zeronfinity.cpfy.common.FILTER_DATE_TIME_FORMAT
import com.zeronfinity.cpfy.common.makeDurationText
import com.zeronfinity.cpfy.databinding.FragmentFiltersBinding
import com.zeronfinity.cpfy.view.adapter.AdapterDisabledPlatformFilters
import com.zeronfinity.cpfy.view.adapter.AdapterEnabledPlatformFilters
import com.zeronfinity.cpfy.view.base.BaseFragment
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import com.zeronfinity.cpfy.viewmodel.FiltersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import javax.inject.Inject

typealias AdapterPlatformFilters = Either<AdapterDisabledPlatformFilters, AdapterEnabledPlatformFilters>

@AndroidEntryPoint
class FiltersFragment : BaseFragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!

    private lateinit var filtersViewModel: FiltersViewModel
    private lateinit var contentListViewModel: ContestListViewModel

    @Inject
    lateinit var adapterDisabledPlatformFilters: AdapterDisabledPlatformFilters

    @Inject
    lateinit var adapterEnabledPlatformFilters: AdapterEnabledPlatformFilters

    private val simpleDateFormat = SimpleDateFormat(
        FILTER_DATE_TIME_FORMAT,
        Locale.getDefault()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("onCreateView() started")
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated() started")

        setUpRecyclerView(
            binding.rvDisabledPlatforms,
            binding.ivExpandDisabled,
            Either.Left(adapterDisabledPlatformFilters)
        )
        setUpRecyclerView(
            binding.rvEnabledPlatforms,
            binding.ivExpandEnabled,
            Either.Right(adapterEnabledPlatformFilters)
        )

        contentListViewModel =
            ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeContestListViewModel()

        filtersViewModel = ViewModelProvider(requireActivity()).get(FiltersViewModel::class.java)
        observeFilterDialogViewModel()

        filtersViewModel.loadIsLowerBoundToday()
        filtersViewModel.loadDurationBasedFilterButtonTexts()

        setUpViews()
    }

    private fun setUpRecyclerView(
        rvPlatforms: RecyclerView,
        ivExpand: ImageView,
        adapter: AdapterPlatformFilters
    ) {
        rvPlatforms.layoutManager = GridLayoutManager(activity, 3)
        rvPlatforms.setHasFixedSize(true)
        contractRecyclerView(ivExpand, adapter)

        when (adapter) {
            is Either.Left -> rvPlatforms.adapter = adapter.left
            is Either.Right -> rvPlatforms.adapter = adapter.right
        }

        ivExpand.setOnClickListener {
            when (it.tag) {
                "contracted" -> {
                    expandRecyclerView(
                        ivExpand,
                        adapter
                    )
                }
                "expanded" -> {
                    contractRecyclerView(
                        ivExpand,
                        adapter
                    )
                }
                else -> {
                    logE("Invalid ivExpand tag: " + it.tag.toString())
                }
            }
        }
    }

    private fun contractRecyclerView(
        ivExpand: ImageView,
        adapter: AdapterPlatformFilters
    ) {
        ivExpand.tag = "contracted"
        ivExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
        when (adapter) {
            is Either.Left -> adapter.left.setMaxItemCount()
            is Either.Right -> adapter.right.setMaxItemCount()
        }
    }

    private fun expandRecyclerView(
        ivExpand: ImageView,
        adapter: AdapterPlatformFilters
    ) {
        ivExpand.tag = "expanded"
        ivExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
        when (adapter) {
            is Either.Left -> adapter.left.resetMaxItemCount()
            is Either.Right -> adapter.right.resetMaxItemCount()
        }
    }

    private fun updateVisibility(
        tvLabel: TextView,
        rvPlatforms: RecyclerView,
        ivExpand: ImageView,
        visibility: Int
    ) {
        tvLabel.visibility = visibility
        rvPlatforms.visibility = visibility
        ivExpand.visibility = visibility
    }

    private fun observeContestListViewModel() {
        contentListViewModel.platformListLiveData.observe(viewLifecycleOwner, {
            logD("platformListLiveData -> platformList[$it]")
            val disabledPlatforms = mutableListOf<Platform>()
            val enabledPlatforms = mutableListOf<Platform>()

            for (platform in it) {
                when (platform.isEnabled) {
                    true -> enabledPlatforms.add(platform)
                    false -> disabledPlatforms.add(platform)
                }
            }

            if (disabledPlatforms.size > 0) {
                updateVisibility(
                    binding.tvDisabledPlatformsLabel,
                    binding.rvDisabledPlatforms,
                    binding.ivExpandDisabled,
                    View.VISIBLE
                )
                adapterDisabledPlatformFilters.refreshPlatformList(disabledPlatforms)
            } else {
                updateVisibility(
                    binding.tvDisabledPlatformsLabel,
                    binding.rvDisabledPlatforms,
                    binding.ivExpandDisabled,
                    View.GONE
                )

                binding.btnAllPlatforms.tag = 1
                binding.btnAllPlatforms.text = getString(R.string.disable_all_platforms)
            }

            if (enabledPlatforms.size > 0) {
                updateVisibility(
                    binding.tvEnabledPlatformsLabel,
                    binding.rvEnabledPlatforms,
                    binding.ivExpandEnabled,
                    View.VISIBLE
                )
                adapterEnabledPlatformFilters.refreshPlatformList(enabledPlatforms)
            } else {
                updateVisibility(
                    binding.tvEnabledPlatformsLabel,
                    binding.rvEnabledPlatforms,
                    binding.ivExpandEnabled,
                    View.GONE
                )

                binding.btnAllPlatforms.tag = 0
                binding.btnAllPlatforms.text = getString(R.string.enable_all_platforms)
            }
        })
    }

    private fun observeFilterDialogViewModel() {
        filtersViewModel.startTimeLowerBoundLiveData.observe(viewLifecycleOwner, {
            if (!binding.cbStartTime.isChecked) {
                binding.btnStartTimeLowerBound.text = it
            }
        })

        filtersViewModel.startTimeUpperBoundLiveData.observe(viewLifecycleOwner, {
            if (!binding.cbStartTime.isChecked) {
                binding.btnStartTimeUpperBound.text = it
            }
        })

        filtersViewModel.endTimeLowerBoundLiveData.observe(viewLifecycleOwner, {
            if (!binding.cbEndTime.isChecked) {
                binding.btnEndTimeLowerBound.text = it
            }
        })

        filtersViewModel.endTimeUpperBoundLiveData.observe(viewLifecycleOwner, {
            if (!binding.cbEndTime.isChecked) {
                binding.btnEndTimeUpperBound.text = it
            }
        })

        filtersViewModel.durationLowerBoundLiveData.observe(viewLifecycleOwner, {
            binding.btnDurationLowerBound.text = makeDurationText(it)
        })

        filtersViewModel.durationUpperBoundLiveData.observe(viewLifecycleOwner, {
            binding.btnDurationUpperBound.text = makeDurationText(it)
        })

        filtersViewModel.isStartTimeLowerBoundTodayLiveData.observe(viewLifecycleOwner, {
            processStartTimeLowerBoundToday(it)
        })

        filtersViewModel.isEndTimeLowerBoundTodayLiveData.observe(viewLifecycleOwner, {
            processEndTimeLowerBoundToday(it)
        })

        filtersViewModel.startTimeDaysAfterTodayLiveData.observe(viewLifecycleOwner, {
            binding.tvDaysAfterTodaySt.text = it.toString()

            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.add(Calendar.DAY_OF_YEAR, it)
            binding.btnStartTimeUpperBound.text = simpleDateFormat.format(calendar.time)
        })

        filtersViewModel.endTimeDaysAfterTodayLiveData.observe(viewLifecycleOwner, {
            binding.tvDaysAfterTodayEd.text = it.toString()

            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.add(Calendar.DAY_OF_YEAR, it)
            binding.btnEndTimeUpperBound.text = simpleDateFormat.format(calendar.time)
        })
    }

    private fun setUpViews() {
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

        binding.cbStartTime.setOnClickListener {
            val checkBox = it as CheckBox

            if (checkBox.isChecked) {
                filtersViewModel.setStartTimeLowerBoundToday(true)
                processStartTimeLowerBoundToday(true)
            } else {
                filtersViewModel.setStartTimeLowerBoundToday(false)
                processStartTimeLowerBoundToday(false)
            }
        }

        binding.cbEndTime.setOnClickListener {
            val checkBox = it as CheckBox

            if (checkBox.isChecked) {
                filtersViewModel.setEndTimeLowerBoundToday(true)
                processEndTimeLowerBoundToday(true)
            } else {
                filtersViewModel.setEndTimeLowerBoundToday(false)
                processEndTimeLowerBoundToday(false)
            }
        }

        binding.tvDaysAfterTodaySt.setOnClickListener {
            showDayPicker(START_TIME)
        }

        binding.tvDaysAfterTodayEd.setOnClickListener {
            showDayPicker(END_TIME)
        }
    }

    private fun showDatePicker(filterTimeEnum: FilterTimeEnum, prevDate: Date) {
        val calendar = GregorianCalendar.getInstance()
        calendar.time = prevDate

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        context?.let {
            DatePickerDialog(
                it,
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    showTimePicker(filterTimeEnum, prevDate, GregorianCalendar(year, month, day))
                },
                year,
                month,
                day
            ).show()
        }
    }

    private fun showTimePicker(
        filterTimeEnum: FilterTimeEnum,
        prevDate: Date,
        calendar: GregorianCalendar
    ) {
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
                    when (filterTimeEnum) {
                        START_TIME_LOWER_BOUND, START_TIME_UPPER_BOUND ->
                            filtersViewModel.setStartTimeLowerBoundToday(false)
                        END_TIME_LOWER_BOUND, END_TIME_UPPER_BOUND ->
                            filtersViewModel.setEndTimeLowerBoundToday(false)
                    }
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

    private fun showDayPicker(filterTimeTypeEnum: FilterTimeTypeEnum) {
        val action = FiltersFragmentDirections.actionFiltersFragmentToDayPickerDiaFrag(
            filterTimeTypeEnum
        )
        findNavController().safeNavigate(action)
    }

    private fun processStartTimeLowerBoundToday(value: Boolean) {
        logD("processStartTimeLowerBoundToday() started -> value: [$value]")
        if (value) {
            binding.cbStartTime.isChecked = true
            binding.tvDaysAfterTodaySt.visibility = View.VISIBLE
            binding.tvDaysAfterTodayLabelSt.visibility = View.VISIBLE

            filtersViewModel.loadStartTimeDaysAfterToday()

            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            binding.btnStartTimeLowerBound.text = simpleDateFormat.format(calendar.time)
        } else {
            binding.cbStartTime.isChecked = false
            binding.tvDaysAfterTodaySt.visibility = View.INVISIBLE
            binding.tvDaysAfterTodayLabelSt.visibility = View.INVISIBLE

            filtersViewModel.loadStartTimeBasedFilterButtonTexts()
        }
    }

    private fun processEndTimeLowerBoundToday(value: Boolean) {
        logD("processEndTimeLowerBoundToday() started -> value: [$value]")
        if (value) {
            binding.cbEndTime.isChecked = true
            binding.tvDaysAfterTodayEd.visibility = View.VISIBLE
            binding.tvDaysAfterTodayLabelEd.visibility = View.VISIBLE

            filtersViewModel.loadEndTimeDaysAfterToday()

            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            binding.btnEndTimeLowerBound.text = simpleDateFormat.format(calendar.time)
        } else {
            binding.cbEndTime.isChecked = false
            binding.tvDaysAfterTodayEd.visibility = View.INVISIBLE
            binding.tvDaysAfterTodayLabelEd.visibility = View.INVISIBLE

            filtersViewModel.loadEndTimeBasedFilterButtonTexts()
        }
    }
}
