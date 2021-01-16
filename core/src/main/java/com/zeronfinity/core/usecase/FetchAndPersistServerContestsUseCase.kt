package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.ServerContestInfoResponse.ResponseStatus.SUCCESS
import com.zeronfinity.core.repository.ContestRepository
import com.zeronfinity.core.repository.CookieRepository
import com.zeronfinity.core.repository.ServerContestInfoRepository

class FetchAndPersistServerContestsUseCase(
    private val contestRepository: ContestRepository,
    private val cookieRepository: CookieRepository,
    private val serverContestInfoRepository: ServerContestInfoRepository
) {
    sealed class Result {
        data class Success(val value: Boolean) : Result()
        data class Error(val errorMsg: String) : Result()
        data class UnauthorizedError(val errorCode: Int) : Result()
    }

    suspend operator fun invoke(params: Map<String, String>): Result {
        val serverContestInfoResponse = serverContestInfoRepository.getContestInfo(params)

        return if (serverContestInfoResponse.responseStatus == SUCCESS) {
            contestRepository.removeAllContests()
            serverContestInfoResponse.contestList?.let { contestRepository.addContestList(it) }
            Result.Success(true)
        } else {
            when {
                serverContestInfoResponse.errorCode != null ->
                    when (serverContestInfoResponse.errorCode) {
                        401 -> Result.UnauthorizedError(serverContestInfoResponse.errorCode)
                        429 -> {
                            when (cookieRepository.getCookie("clist_session_cookie")) {
                                null -> Result.UnauthorizedError(serverContestInfoResponse.errorCode)
                                else -> Result.Error("API Limit Reached!\nPlease try again 1 minute later.")
                            }
                        }
                        else -> Result.Error("Error ${serverContestInfoResponse.errorCode}: ${serverContestInfoResponse.errorDesc}")
                    }
                serverContestInfoResponse.errorDesc != null ->
                    Result.Error(serverContestInfoResponse.errorDesc)
                else -> Result.Error("Unknown Network Error!")
            }
        }
    }
}
