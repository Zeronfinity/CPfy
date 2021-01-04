package com.zeronfinity.core.repository

import java.util.Date

interface FilterTimeRangeDataSource {
    fun isSaved(): Boolean

    fun setSaved(value: Boolean)

    fun getStartTimeLowerBound(): Date

    fun getStartTimeUpperBound(): Date

    fun getEndTimeLowerBound(): Date

    fun getEndTimeUpperBound(): Date

    fun getDurationLowerBound(): Int

    fun getDurationUpperBound(): Int

    fun setStartTimeLowerBound(date: Date)

    fun setStartTimeUpperBound(date: Date)

    fun setEndTimeLowerBound(date: Date)

    fun setEndTimeUpperBound(date: Date)

    fun setDurationLowerBound(duration: Int)

    fun setDurationUpperBound(duration: Int)
}
