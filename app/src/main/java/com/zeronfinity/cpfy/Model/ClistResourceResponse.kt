package com.zeronfinity.cpfy.Model

import com.squareup.moshi.Json

data class ClistResourceResponse (
    @field:Json(name = "icon")
    var iconUrlSegment: String,
    @field:Json(name = "name")
    var platformName: String
)
