package com.zeronfinity.core.repository

import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.END_TIME
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.START_TIME
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import java.util.Date

class FilterTimeRangeRepository(private val filterTimeRangeDataSource: FilterTimeRangeDataSource) {
    enum class FilterTimeEnum {
        START_TIME_LOWER_BOUND,
        START_TIME_UPPER_BOUND,
        END_TIME_LOWER_BOUND,
        END_TIME_UPPER_BOUND,
    }

    enum class FilterDurationEnum {
        DURATION_LOWER_BOUND,
        DURATION_UPPER_BOUND
    }

    enum class FilterTimeTypeEnum {
        START_TIME,
        END_TIME
    }

    fun getFilterTimeRange(filterTimeEnum: FilterTimeEnum) = when (filterTimeEnum) {
        START_TIME_LOWER_BOUND -> filterTimeRangeDataSource.getStartTimeLowerBound()
        START_TIME_UPPER_BOUND -> filterTimeRangeDataSource.getStartTimeUpperBound()
        END_TIME_LOWER_BOUND -> filterTimeRangeDataSource.getEndTimeLowerBound()
        END_TIME_UPPER_BOUND -> filterTimeRangeDataSource.getEndTimeUpperBound()
    }

    fun getFilterDuration(filterDurationEnum: FilterDurationEnum) = when (filterDurationEnum) {
        DURATION_LOWER_BOUND -> filterTimeRangeDataSource.getDurationLowerBound()
        DURATION_UPPER_BOUND -> filterTimeRangeDataSource.getDurationUpperBound()
    }

    fun setFilterTimeRange(filterTimeEnum: FilterTimeEnum, date: Date) = when (filterTimeEnum) {
        START_TIME_LOWER_BOUND -> filterTimeRangeDataSource.setStartTimeLowerBound(date)
        START_TIME_UPPER_BOUND -> filterTimeRangeDataSource.setStartTimeUpperBound(date)
        END_TIME_LOWER_BOUND -> filterTimeRangeDataSource.setEndTimeLowerBound(date)
        END_TIME_UPPER_BOUND -> filterTimeRangeDataSource.setEndTimeUpperBound(date)
    }

    fun setFilterDuration(filterDurationEnum: FilterDurationEnum, duration: Int) = when (filterDurationEnum) {
        DURATION_LOWER_BOUND -> filterTimeRangeDataSource.setDurationLowerBound(duration)
        DURATION_UPPER_BOUND -> filterTimeRangeDataSource.setDurationUpperBound(duration)
    }

    fun isSaved() = filterTimeRangeDataSource.isSaved()

    fun setSaved(value: Boolean) = filterTimeRangeDataSource.setSaved(value)

    fun isLowerBoundToday(filterTimeTypeEnum: FilterTimeTypeEnum) = when (filterTimeTypeEnum) {
        START_TIME -> filterTimeRangeDataSource.isStartTimeLowerBoundToday()
        END_TIME -> filterTimeRangeDataSource.isEndTimeLowerBoundToday()
    }

    fun getDaysAfterToday(filterTimeTypeEnum: FilterTimeTypeEnum) = when (filterTimeTypeEnum) {
        START_TIME -> filterTimeRangeDataSource.getStartTimeDaysAfterToday()
        END_TIME -> filterTimeRangeDataSource.getEndTimeDaysAfterToday()
    }

    fun setLowerBoundToday(filterTimeTypeEnum: FilterTimeTypeEnum, value: Boolean) = when (filterTimeTypeEnum) {
        START_TIME -> filterTimeRangeDataSource.setStartTimeLowerBoundToday(value)
        END_TIME -> filterTimeRangeDataSource.setEndTimeLowerBoundToday(value)
    }

    fun setDaysAfterToday(filterTimeTypeEnum: FilterTimeTypeEnum, value: Int) = when (filterTimeTypeEnum) {
        START_TIME -> filterTimeRangeDataSource.setStartTimeDaysAfterToday(value)
        END_TIME -> filterTimeRangeDataSource.setEndTimeDaysAfterToday(value)
    }
}
