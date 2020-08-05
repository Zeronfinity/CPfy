package com.zeronfinity.cpfy.model.network.pojo

import com.squareup.moshi.Json

data class ClistServerResponse(
    @field:Json(name = "objects")
    var contestList: List<ClistContestObjectResponse>
)
