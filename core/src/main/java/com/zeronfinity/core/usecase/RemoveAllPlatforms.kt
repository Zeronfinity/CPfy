package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class RemoveAllPlatforms(private val platformRepository: PlatformRepository) {
    operator fun invoke() = platformRepository.removeAllPlatforms()
}
