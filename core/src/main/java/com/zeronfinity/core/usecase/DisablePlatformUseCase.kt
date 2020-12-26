package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class DisablePlatformUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke(platformName: String) = platformRepository.disablePlatform(platformName)
}
