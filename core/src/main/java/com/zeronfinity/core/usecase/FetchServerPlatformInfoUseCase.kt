package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.ServerPlatformInfoResponse.ResponseStatus.NOT_FIRST_TIME
import com.zeronfinity.core.entity.ServerPlatformInfoResponse.ResponseStatus.SUCCESS
import com.zeronfinity.core.repository.PlatformRepository
import com.zeronfinity.core.repository.ServerPlatformInfoRepository

class FetchServerPlatformInfoUseCase(
    private val platformRepository: PlatformRepository,
    private val serverPlatformInfoRepository: ServerPlatformInfoRepository
) {
    sealed class Result {
        data class Success(val isUpdateRequired: Boolean) : Result()
        data class Error(val errorMsg: String) : Result()
        data class UnauthorizedError(val errorCode: Int) : Result()
    }

    suspend operator fun invoke(): Result {
        val serverPlatformInfoResponse = serverPlatformInfoRepository.getPlatformInfo()

        return if (serverPlatformInfoResponse.responseStatus == SUCCESS) {
            serverPlatformInfoResponse.platformList?.let { platformRepository.addPlatformList(it) }
            Result.Success(true)
        } else if (serverPlatformInfoResponse.responseStatus == NOT_FIRST_TIME) {
            Result.Success(false)
        } else {
            if (serverPlatformInfoResponse.errorCode != null) {
                when (serverPlatformInfoResponse.errorCode) {
                    401 -> Result.UnauthorizedError(serverPlatformInfoResponse.errorCode)
                    else -> Result.Error("Error ${serverPlatformInfoResponse.errorCode}: ${serverPlatformInfoResponse.errorDesc}")
                }
            } else if (serverPlatformInfoResponse.errorDesc != null) {
                Result.Error(serverPlatformInfoResponse.errorDesc)
            } else {
                Result.Error("Unknown Network Error!")
            }
        }
    }
}
