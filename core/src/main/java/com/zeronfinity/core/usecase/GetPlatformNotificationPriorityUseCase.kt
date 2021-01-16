package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class GetPlatformNotificationPriorityUseCase(private val platformRepository: PlatformRepository) {
    suspend operator fun invoke(platformId: Int) = platformRepository.getNotificationPriority(platformId)
}
