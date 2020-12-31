package com.zeronfinity.core.entity


data class ServerPlatformInfoResponse (
    val responseStatus: ResponseStatus,
    val errorCode: Int?,
    val errorDesc: String?,
    val platformList: List<Platform>?
) {
    enum class ResponseStatus { SUCCESS, FAILURE, NOT_FIRST_TIME }
}
