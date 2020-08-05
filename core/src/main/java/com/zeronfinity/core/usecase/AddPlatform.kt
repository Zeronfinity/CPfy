package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.repository.PlatformRepository

class AddPlatform(private val platformRepository: PlatformRepository) {
    operator fun invoke(platform: Platform) = platformRepository.addPlatform(platform)
}
