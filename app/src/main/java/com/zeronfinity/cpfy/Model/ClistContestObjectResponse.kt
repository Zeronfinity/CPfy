package com.zeronfinity.cpfy.Model

import com.squareup.moshi.Json

data class ClistContestObjectResponse (
    @field:Json(name = "event")
    var title: String,
    @field:Json(name = "start")
    var start: String,
    @field:Json(name = "end")
    var end: String,
    @field:Json(name = "duration")
    var url: String,
    @field:Json(name = "resource")
    var platformResource: ClistResourceResponse
)
