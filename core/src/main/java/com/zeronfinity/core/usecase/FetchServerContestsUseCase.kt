package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.entity.ServerContestInfoResponse.ResponseStatus.SUCCESS
import com.zeronfinity.core.repository.ServerContestInfoRepository

class FetchServerContestsUseCase(
    private val serverContestInfoRepository: ServerContestInfoRepository
) {
    suspend operator fun invoke(params: Map<String, String>): List<Contest>? {
        val serverContestInfoResponse = serverContestInfoRepository.getContestInfo(params)

        return if (serverContestInfoResponse.responseStatus == SUCCESS) {
            serverContestInfoResponse.contestList
        } else {
            null
        }
    }
}
