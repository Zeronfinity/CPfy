package com.zeronfinity.cpfy.framework.network.pojo

import com.squareup.moshi.Json

data class ClistServerResponseContests(
    @field:Json(name = "objects")
    var contestList: List<ClistContestObjectResponse>
)
