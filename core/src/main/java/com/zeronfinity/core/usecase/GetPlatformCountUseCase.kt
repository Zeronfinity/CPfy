package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class GetPlatformCountUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke() = platformRepository.getPlatformCount()
}
