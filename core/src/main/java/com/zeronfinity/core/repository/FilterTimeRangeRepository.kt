package com.zeronfinity.core.repository

import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import java.util.Date

class FilterTimeRangeRepository(private val filterTimeRangeDataSource: FilterTimeRangeDataSource) {
    enum class FilterTimeEnum {
        START_TIME_LOWER_BOUND,
        START_TIME_UPPER_BOUND,
        END_TIME_LOWER_BOUND,
        END_TIME_UPPER_BOUND,
        DURATION_LOWER_BOUND,
        DURATION_UPPER_BOUND
    }

    fun getFilterTimeRange(filterTimeEnum: FilterTimeEnum) = when (filterTimeEnum) {
        START_TIME_LOWER_BOUND -> filterTimeRangeDataSource.getStartTimeLowerBound()
        START_TIME_UPPER_BOUND -> filterTimeRangeDataSource.getStartTimeUpperBound()
        END_TIME_LOWER_BOUND -> filterTimeRangeDataSource.getEndTimeLowerBound()
        END_TIME_UPPER_BOUND -> filterTimeRangeDataSource.getEndTimeUpperBound()
        DURATION_LOWER_BOUND -> filterTimeRangeDataSource.getDurationLowerBound()
        DURATION_UPPER_BOUND -> filterTimeRangeDataSource.getDurationUpperBound()
    }

    fun setFilterTimeRange(filterTimeEnum: FilterTimeEnum, date: Date) = when (filterTimeEnum) {
        START_TIME_LOWER_BOUND -> filterTimeRangeDataSource.setStartTimeLowerBound(date)
        START_TIME_UPPER_BOUND -> filterTimeRangeDataSource.setStartTimeUpperBound(date)
        END_TIME_LOWER_BOUND -> filterTimeRangeDataSource.setEndTimeLowerBound(date)
        END_TIME_UPPER_BOUND -> filterTimeRangeDataSource.setEndTimeUpperBound(date)
        DURATION_LOWER_BOUND -> filterTimeRangeDataSource.setDurationLowerBound(date)
        DURATION_UPPER_BOUND -> filterTimeRangeDataSource.setDurationUpperBound(date)
    }
}
