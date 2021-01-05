package com.zeronfinity.core.repository

import java.util.Date

interface FilterTimeRangeDataSource {
    fun isSaved(): Boolean

    fun setSaved(value: Boolean)

    fun getStartTimeLowerBound(): Date

    fun isStartTimeLowerBoundToday(): Boolean

    fun getStartTimeDaysAfterToday(): Int

    fun getStartTimeUpperBound(): Date

    fun getEndTimeLowerBound(): Date

    fun isEndTimeLowerBoundToday(): Boolean

    fun getEndTimeDaysAfterToday(): Int

    fun getEndTimeUpperBound(): Date

    fun getDurationLowerBound(): Int

    fun getDurationUpperBound(): Int

    fun setStartTimeLowerBound(date: Date)

    fun setStartTimeLowerBoundToday(value: Boolean)

    fun setStartTimeDaysAfterToday(daysAfterToday: Int)

    fun setStartTimeUpperBound(date: Date)

    fun setEndTimeLowerBound(date: Date)

    fun setEndTimeLowerBoundToday(value: Boolean)

    fun setEndTimeDaysAfterToday(daysAfterToday: Int)

    fun setEndTimeUpperBound(date: Date)

    fun setDurationLowerBound(duration: Int)

    fun setDurationUpperBound(duration: Int)
}
