package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class GetPlatformUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke(platformName: String) = platformRepository.getPlatform(platformName)
}
