package com.zeronfinity.cpfy.framework.implementations

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.entity.ServerContestInfoResponse
import com.zeronfinity.core.entity.ServerContestInfoResponse.ResponseStatus.FAILURE
import com.zeronfinity.core.entity.ServerContestInfoResponse.ResponseStatus.SUCCESS
import com.zeronfinity.core.entity.createPlatformShortName
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.ServerContestInfoDataSource
import com.zeronfinity.cpfy.framework.network.ResultWrapper
import com.zeronfinity.cpfy.framework.network.ClistNetworkCall
import com.zeronfinity.cpfy.framework.network.pojo.ClistServerResponseContests

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
        var clistServerResponseContests: ClistServerResponseContests? = null

        logD("getInfo() -> wrappedResult: [$wrappedResult]")

        when (wrappedResult) {
            is ResultWrapper.Success -> {
                responseStatus = SUCCESS
                clistServerResponseContests = wrappedResult.value.body()
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

        logD("clistServerResponse: [" + clistServerResponseContests.toString() + "]")

        if (clistServerResponseContests != null) {
            val list = clistServerResponseContests.contestList
            contestList = ArrayList()
            platformList = ArrayList()

            for (i in list.indices) {
                contestList.add(list[i].toContest())
                platformList.add(Platform(
                    list[i].platformResourceObject.platformId,
                    list[i].platformResourceObject.platformName,
                    "https://clist.by" + list[i].platformResourceObject.iconUrlSegment,
                    createPlatformShortName(list[i].platformResourceObject.platformName)
                ))
            }
        }

        logD("getInfo() -> contestList: [$contestList]")
        logD("getInfo() -> platformList: [$platformList]")

        return ServerContestInfoResponse(responseStatus, errorCode, errorDesc, contestList, platformList)
    }
}
