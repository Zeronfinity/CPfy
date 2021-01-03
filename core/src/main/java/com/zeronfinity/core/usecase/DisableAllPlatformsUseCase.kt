package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class DisableAllPlatformsUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke() = platformRepository.disableAllPlatforms()
}
