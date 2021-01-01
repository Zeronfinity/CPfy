package com.zeronfinity.cpfy.framework.network.pojo

import com.squareup.moshi.Json

data class ClistResourceObjectResponse (
    @field:Json(name = "id")
    var platformId: Int,
    @field:Json(name = "icon")
    var iconUrlSegment: String,
    @field:Json(name = "name")
    var platformName: String
)
