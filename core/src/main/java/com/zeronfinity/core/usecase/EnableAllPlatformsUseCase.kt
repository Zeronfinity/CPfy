package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class EnableAllPlatformsUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke() = platformRepository.enableAllPlatforms()
}
