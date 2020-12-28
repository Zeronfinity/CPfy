package com.zeronfinity.core.entity

import java.util.Date

data class Contest(
    val name: String,
    val duration: Int,
    val platformName: String,
    val startTime: Date,
    val endTime: Date,
    val url: String
)
