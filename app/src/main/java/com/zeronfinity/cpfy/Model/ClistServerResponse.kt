package com.zeronfinity.cpfy.Model

import com.squareup.moshi.Json

data class ClistServerResponse(
    @field:Json(name = "objects")
    var contestList: List<ClistContestObjectResponse>
)
