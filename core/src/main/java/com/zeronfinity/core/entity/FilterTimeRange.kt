package com.zeronfinity.core.entity

import java.util.Date

data class FilterTimeRange(
    val startTimeLowerBound: Date,
    val startTimeUpperBound: Date,
    val endTimeLowerBound: Date,
    val endTimeUpperBound: Date,
    val durationLowerBound: Date,
    val durationUpperBound: Date
)
