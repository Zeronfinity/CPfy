package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class IsPlatformEnabledUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke(platformId: Int) = platformRepository.isPlatformEnabled(platformId)
}
