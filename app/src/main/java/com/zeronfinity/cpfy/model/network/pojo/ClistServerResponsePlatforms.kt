package com.zeronfinity.cpfy.model.network.pojo

import com.squareup.moshi.Json

data class ClistServerResponsePlatforms(
    @field:Json(name = "objects")
    var platformList: List<ClistResourceObjectResponse>
)
