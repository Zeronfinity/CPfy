package com.zeronfinity.cpfy.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.usecase.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FiltersViewModel @ViewModelInject constructor(
    private val disablePlatformUseCase: DisablePlatformUseCase,
    private val enablePlatformUseCase: EnablePlatformUseCase,
    private val getFilterDurationUseCase: GetFilterDurationUseCase,
    private val setFilterDurationUseCase: SetFilterDurationUseCase,
    private val getFilterTimeUseCase: GetFilterTimeUseCase,
    private val setFilterTimeUseCase: SetFilterTimeUseCase,
    private val getPlatformListUseCase: GetPlatformListUseCase
) : ViewModel() {
    private val LOG_TAG = FiltersViewModel::class.simpleName

    private val numberOfDaysBeforeContestsEnd = 7

    val startTimeLowerBoundLiveData = MutableLiveData<String>()
    val startTimeUpperBoundLiveData = MutableLiveData<String>()
    val endTimeLowerBoundLiveData = MutableLiveData<String>()
    val endTimeUpperBoundLiveData = MutableLiveData<String>()
    val durationLowerBoundLiveData = MutableLiveData<Int>()
    val durationUpperBoundLiveData = MutableLiveData<Int>()

    val platformListUpdatedLiveData = MutableLiveData<Boolean>()

    private val simpleDateFormat = SimpleDateFormat(
        "dd-MM-yy\nhh:mm a",
        Locale.getDefault()
    )

    fun setTimeFilters(filterTimeEnum: FilterTimeEnum, date: Date) {
        Log.d(LOG_TAG, "setTimeFilters called: filterTimeEnum = [${filterTimeEnum.name}], date = [$date]")

        setFilterTimeUseCase(filterTimeEnum, date)

        when (filterTimeEnum) {
            START_TIME_LOWER_BOUND -> loadStartTimeLowerBoundBtnText()
            START_TIME_UPPER_BOUND -> loadStartTimeUpperBoundBtnText()
            END_TIME_LOWER_BOUND -> loadEndTimeLowerBoundBtnText()
            END_TIME_UPPER_BOUND -> loadEndTimeUpperBoundBtnText()
        }
    }

    fun setDurationFilters(filterDurationEnum: FilterDurationEnum, duration: Int) {
        Log.d(LOG_TAG, "setDurationFilters called: filterDurationEnum = [${filterDurationEnum.name}], duration = [$duration]")

        setFilterDurationUseCase(filterDurationEnum, duration)

        when (filterDurationEnum) {
            DURATION_LOWER_BOUND -> loadDurationLowerBoundBtnText()
            DURATION_UPPER_BOUND -> loadDurationUpperBoundBtnText()
        }
    }

    fun getDurationFilters(filterDurationEnum: FilterDurationEnum) = getFilterDurationUseCase(filterDurationEnum)

    fun loadTimeBasedFilterButtonTexts() {
        loadStartTimeLowerBoundBtnText()
        loadStartTimeUpperBoundBtnText()
        loadEndTimeLowerBoundBtnText()
        loadEndTimeUpperBoundBtnText()
        loadDurationLowerBoundBtnText()
        loadDurationUpperBoundBtnText()
    }

    fun enableAllPlatforms() {
        val platformList = getPlatformListUseCase()
        for (platform in platformList) {
            enablePlatformUseCase(platform.name)
        }
        platformListUpdatedLiveData.postValue(true)
    }

    fun disableAllPlatforms() {
        val platformList = getPlatformListUseCase()
        for (platform in platformList) {
            disablePlatformUseCase(platform.name)
        }
        platformListUpdatedLiveData.postValue(true)
    }

    fun resetAllFilters() {
        enableAllPlatforms()

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        setTimeFilters(START_TIME_LOWER_BOUND, Date())
        setTimeFilters(START_TIME_UPPER_BOUND, calendar.time)

        setTimeFilters(END_TIME_LOWER_BOUND, Date())
        setTimeFilters(END_TIME_UPPER_BOUND, calendar.time)

        setDurationFilters(DURATION_LOWER_BOUND, 0)
        setDurationFilters(DURATION_UPPER_BOUND, 7*24*60*60)

        platformListUpdatedLiveData.postValue(true)
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
