package com.zeronfinity.cpfy.model

import android.app.Application
import com.zeronfinity.core.entity.*
import com.zeronfinity.core.entity.ServerPlatformInfoResponse.ResponseStatus.*
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.ServerPlatformInfoDataSource
import com.zeronfinity.cpfy.CustomApplication
import com.zeronfinity.cpfy.framework.network.ResultWrapper
import com.zeronfinity.cpfy.model.network.ClistNetworkCall
import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponsePlatforms

class ServerPlatformInfoClist(
    private val application: CustomApplication,
    private val clistNetworkCall: ClistNetworkCall
) : ServerPlatformInfoDataSource {
    override suspend fun getInfo(): ServerPlatformInfoResponse {
        if (application.isPlatformListFetched) {
            return ServerPlatformInfoResponse(NOT_FIRST_TIME, null, null, null)
        }

        var responseStatus = FAILURE
        var errorCode: Int? = null
        var errorDesc: String? = null
        var platformList: MutableList<Platform>? = null

        val wrappedResult = clistNetworkCall.getPlatformData()
        var clistServerResponsePlatforms: ClistServerResponsePlatforms? = null

        logD("getInfo() -> wrappedResult: [$wrappedResult]")

        when (wrappedResult) {
            is ResultWrapper.Success -> {
                responseStatus = SUCCESS
                clistServerResponsePlatforms = wrappedResult.value.body()
            }
            is ResultWrapper.GenericError -> {
                if (wrappedResult.code != null) {
                    errorCode = wrappedResult.code
                    errorDesc = wrappedResult.error?.error_description
                }
            }
            is ResultWrapper.NetworkError -> {
                errorDesc = "Network Failure: IOException!"
            }
        }

        logD("clistServerResponse: [${clistServerResponsePlatforms.toString()}]")

        if (clistServerResponsePlatforms != null) {
            val list = clistServerResponsePlatforms.platformList
            platformList = ArrayList()

            for (i in list.indices) {
                platformList.add(Platform(list[i].platformName,
                    "https://clist.by" + list[i].iconUrlSegment,
                    createPlatformShortName(list[i].platformName)
                ))
            }
        }

        logD("getInfo() -> platformList: [$platformList]")

        return ServerPlatformInfoResponse(responseStatus, errorCode, errorDesc, platformList)
    }
}
