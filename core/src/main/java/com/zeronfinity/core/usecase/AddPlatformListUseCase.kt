package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.repository.PlatformRepository

class AddPlatformListUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke(platformList: List<Platform>) = platformRepository.addPlatformList(platformList)
}
