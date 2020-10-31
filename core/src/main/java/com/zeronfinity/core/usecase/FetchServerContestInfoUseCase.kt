package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ServerContestInfoRepository

class FetchServerContestInfoUseCase(private val serverContestInfoRepository: ServerContestInfoRepository) {
    suspend operator fun invoke(params: Map<String, String>) = serverContestInfoRepository.getContestInfo(params)
}