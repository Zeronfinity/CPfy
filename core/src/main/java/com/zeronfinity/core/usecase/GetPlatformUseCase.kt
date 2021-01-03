package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class GetPlatformUseCase(private val platformRepository: PlatformRepository) {
    suspend operator fun invoke(platformId: Int) = platformRepository.getPlatform(platformId)
}
