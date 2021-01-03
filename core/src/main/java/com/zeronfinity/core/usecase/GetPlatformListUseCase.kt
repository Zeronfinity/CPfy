package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class GetPlatformListUseCase(private val platformRepository: PlatformRepository) {
    suspend operator fun invoke() = platformRepository.getPlatformList()
}
