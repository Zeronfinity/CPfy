package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.ServerContestInfoResponse
import com.zeronfinity.core.repository.ContestRepository
import com.zeronfinity.core.repository.PlatformRepository
import com.zeronfinity.core.repository.ServerContestInfoRepository

class FetchServerContestInfoUseCase(
    private val contestRepository: ContestRepository,
    private val platformRepository: PlatformRepository,
    private val serverContestInfoRepository: ServerContestInfoRepository
) {
    sealed class Result {
        data class Success(val value: Boolean) : Result()
        data class Error(val errorMsg: String) : Result()
        data class UnauthorizedError(val errorCode: Int) : Result()
    }

    suspend operator fun invoke(params: Map<String, String>): Result {
        val serverContestInfoResponse = serverContestInfoRepository.getContestInfo(params)

        return if (serverContestInfoResponse.responseStatus == ServerContestInfoResponse.ResponseStatus.SUCCESS) {
            contestRepository.removeAllContests()
            serverContestInfoResponse.contestList?.let { contestRepository.addContestList(it) }
            serverContestInfoResponse.platformList?.let { platformRepository.addPlatformList(it) }
            Result.Success(true)
        } else {
            if (serverContestInfoResponse.errorCode != null) {
                when (serverContestInfoResponse.errorCode) {
                    401 -> Result.UnauthorizedError(serverContestInfoResponse.errorCode)
                    else -> Result.Error("Error ${serverContestInfoResponse.errorCode}: ${serverContestInfoResponse.errorDesc}")
                }
            } else if (serverContestInfoResponse.errorDesc != null) {
                Result.Error(serverContestInfoResponse.errorDesc)
            } else {
                Result.Error("Unknown Network Error!")
            }
        }
    }
}
