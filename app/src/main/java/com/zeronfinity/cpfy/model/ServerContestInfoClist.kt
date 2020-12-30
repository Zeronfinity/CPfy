package com.zeronfinity.cpfy.model

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.entity.ServerContestInfoResponse
import com.zeronfinity.core.entity.ServerContestInfoResponse.ResponseStatus.FAILURE
import com.zeronfinity.core.entity.ServerContestInfoResponse.ResponseStatus.SUCCESS
import com.zeronfinity.core.entity.createPlatformShortName
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.ServerContestInfoDataSource
import com.zeronfinity.cpfy.framework.network.ResultWrapper
import com.zeronfinity.cpfy.model.network.ClistNetworkCall
import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponse

class ServerContestInfoClist(
    private val clistNetworkCall: ClistNetworkCall
) : ServerContestInfoDataSource {
    override suspend fun getInfo(params: Map<String, String>): ServerContestInfoResponse {
        var responseStatus = FAILURE
        var errorCode: Int? = null
        var errorDesc: String? = null
        var contestList: MutableList<Contest>? = null
        var platformList: MutableList<Platform>? = null

        val wrappedResult = clistNetworkCall.getContestData(params)
        var clistServerResponse: ClistServerResponse? = null

        logD("getInfo() -> wrappedResult: [$wrappedResult]")

        when (wrappedResult) {
            is ResultWrapper.Success -> {
                responseStatus = SUCCESS
                clistServerResponse = wrappedResult.value.body()
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

        logD("clistServerResponse: [" + clistServerResponse.toString() + "]")

        if (clistServerResponse != null) {
            val list = clistServerResponse.contestList
            contestList = ArrayList()
            platformList = ArrayList()

            for (i in list.indices) {
                contestList.add(list[i].toContest())
                platformList.add(Platform(list[i].platformResource.platformName,
                    "https://clist.by" + list[i].platformResource.iconUrlSegment,
                    createPlatformShortName(list[i].platformResource.platformName)
                ))
            }
        }

        logD("getInfo() -> contestList: [$contestList]")
        logD("getInfo() -> platformList: [$contestList]")

        return ServerContestInfoResponse(responseStatus, errorCode, errorDesc, contestList, platformList)
    }
}
