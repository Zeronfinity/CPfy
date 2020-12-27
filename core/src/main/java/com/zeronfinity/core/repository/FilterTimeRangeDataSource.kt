package com.zeronfinity.core.repository

import java.util.Date

interface FilterTimeRangeDataSource {
    fun getStartTimeLowerBound(): Date

    fun getStartTimeUpperBound(): Date

    fun getEndTimeLowerBound(): Date

    fun getEndTimeUpperBound(): Date

    fun getDurationLowerBound(): Date

    fun getDurationUpperBound(): Date

    fun setStartTimeLowerBound(date: Date)

    fun setStartTimeUpperBound(date: Date)

    fun setEndTimeLowerBound(date: Date)

    fun setEndTimeUpperBound(date: Date)

    fun setDurationLowerBound(date: Date)

    fun setDurationUpperBound(date: Date)
}
