package com.zeronfinity.cpfy.model.network.pojo

import com.squareup.moshi.Json

data class ClistResourceObjectResponse (
    @field:Json(name = "icon")
    var iconUrlSegment: String,
    @field:Json(name = "name")
    var platformName: String
)
