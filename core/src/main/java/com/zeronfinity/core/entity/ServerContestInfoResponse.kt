package com.zeronfinity.core.entity


data class ServerContestInfoResponse (
    val responseStatus: ResponseStatus,
    val errorCode: Int?,
    val errorDesc: String?,
    val contestList: List<Contest>?,
    val platformList: List<Platform>?
) {
    enum class ResponseStatus { SUCCESS, FAILURE }
}
