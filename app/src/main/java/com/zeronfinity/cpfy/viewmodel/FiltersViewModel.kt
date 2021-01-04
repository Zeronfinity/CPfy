package com.zeronfinity.cpfy.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.usecase.*
import com.zeronfinity.cpfy.common.DEFAULT_DAYS_INTERVAL
import com.zeronfinity.cpfy.common.DEFAULT_MAX_DURATION
import com.zeronfinity.cpfy.common.DEFAULT_MIN_DURATION
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FiltersViewModel @ViewModelInject constructor(
    private val disableAllPlatformsUseCase: DisableAllPlatformsUseCase,
    private val enableAllPlatformsUseCase: EnableAllPlatformsUseCase,
    private val getFilterDurationUseCase: GetFilterDurationUseCase,
    private val setFilterDurationUseCase: SetFilterDurationUseCase,
    private val getFilterTimeUseCase: GetFilterTimeUseCase,
    private val setFilterTimeUseCase: SetFilterTimeUseCase,
    private val isFilterSavedUseCase: IsFilterSavedUseCase,
    private val setFilterSavedUseCase: SetFilterSavedUseCase
) : ViewModel() {
    private val numberOfDaysBeforeContestsEnd = DEFAULT_DAYS_INTERVAL

    val startTimeLowerBoundLiveData = MutableLiveData<String>()
    val startTimeUpperBoundLiveData = MutableLiveData<String>()
    val endTimeLowerBoundLiveData = MutableLiveData<String>()
    val endTimeUpperBoundLiveData = MutableLiveData<String>()
    val durationLowerBoundLiveData = MutableLiveData<Int>()
    val durationUpperBoundLiveData = MutableLiveData<Int>()

    private val simpleDateFormat = SimpleDateFormat(
        "dd-MM-yy\nhh:mm a",
        Locale.getDefault()
    )

    fun getTimeFilters(filterTimeEnum: FilterTimeEnum) = getFilterTimeUseCase(filterTimeEnum)

    fun setTimeFilters(filterTimeEnum: FilterTimeEnum, date: Date) {
        logD("setTimeFilters called: filterTimeEnum = [${filterTimeEnum.name}], date = [$date]")

        setFilterTimeUseCase(filterTimeEnum, date)

        when (filterTimeEnum) {
            START_TIME_LOWER_BOUND -> loadStartTimeLowerBoundBtnText()
            START_TIME_UPPER_BOUND -> loadStartTimeUpperBoundBtnText()
            END_TIME_LOWER_BOUND -> loadEndTimeLowerBoundBtnText()
            END_TIME_UPPER_BOUND -> loadEndTimeUpperBoundBtnText()
        }
    }

    fun setDurationFilters(filterDurationEnum: FilterDurationEnum, duration: Int) {
        logD("setDurationFilters called: filterDurationEnum = [${filterDurationEnum.name}], duration = [$duration]")

        setFilterDurationUseCase(filterDurationEnum, duration)

        when (filterDurationEnum) {
            DURATION_LOWER_BOUND -> loadDurationLowerBoundBtnText()
            DURATION_UPPER_BOUND -> loadDurationUpperBoundBtnText()
        }
    }

    fun getDurationFilters(filterDurationEnum: FilterDurationEnum) = getFilterDurationUseCase(filterDurationEnum)

    fun isSaved() = isFilterSavedUseCase()

    fun setSaved(value: Boolean) = setFilterSavedUseCase(value)

    fun loadTimeBasedFilterButtonTexts() {
        loadStartTimeLowerBoundBtnText()
        loadStartTimeUpperBoundBtnText()
        loadEndTimeLowerBoundBtnText()
        loadEndTimeUpperBoundBtnText()
        loadDurationLowerBoundBtnText()
        loadDurationUpperBoundBtnText()
    }

    fun enableAllPlatforms() {
        enableAllPlatformsUseCase()
    }

    fun disableAllPlatforms() {
        disableAllPlatformsUseCase()
    }

    fun resetAllFilters() {
        logD("resetAllFilters() started")

        enableAllPlatforms()

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        setTimeFilters(START_TIME_LOWER_BOUND, Date())
        setTimeFilters(START_TIME_UPPER_BOUND, calendar.time)

        setTimeFilters(END_TIME_LOWER_BOUND, Date())
        setTimeFilters(END_TIME_UPPER_BOUND, calendar.time)

        setDurationFilters(DURATION_LOWER_BOUND, DEFAULT_MIN_DURATION)
        setDurationFilters(DURATION_UPPER_BOUND, DEFAULT_MAX_DURATION)

        loadTimeBasedFilterButtonTexts()
    }

    private fun loadStartTimeLowerBoundBtnText() {
        startTimeLowerBoundLiveData.postValue(
            simpleDateFormat.format(getFilterTimeUseCase(START_TIME_LOWER_BOUND))
        )
    }

    private fun loadStartTimeUpperBoundBtnText() {
        startTimeUpperBoundLiveData.postValue(
            simpleDateFormat.format(getFilterTimeUseCase(START_TIME_UPPER_BOUND))
        )
    }

    private fun loadEndTimeLowerBoundBtnText() {
        endTimeLowerBoundLiveData.postValue(
            simpleDateFormat.format(getFilterTimeUseCase(END_TIME_LOWER_BOUND))
        )
    }

    private fun loadEndTimeUpperBoundBtnText() {
        endTimeUpperBoundLiveData.postValue(
            simpleDateFormat.format(getFilterTimeUseCase(END_TIME_UPPER_BOUND))
        )
    }

    private fun loadDurationLowerBoundBtnText() {
        durationLowerBoundLiveData.postValue(
            getFilterDurationUseCase(DURATION_LOWER_BOUND)
        )
    }

    private fun loadDurationUpperBoundBtnText() {
        durationUpperBoundLiveData.postValue(
            getFilterDurationUseCase(DURATION_UPPER_BOUND)
        )
    }
}
