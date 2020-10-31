package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class RemoveAllPlatformsUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke() = platformRepository.removeAllPlatforms()
}
