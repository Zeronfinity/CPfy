package com.zeronfinity.core.entity

import java.util.Date

data class Contest(
    val id: Int,
    val name: String,
    val duration: Int,
    val platformId: Int,
    val startTime: Date,
    val endTime: Date,
    val url: String
)
