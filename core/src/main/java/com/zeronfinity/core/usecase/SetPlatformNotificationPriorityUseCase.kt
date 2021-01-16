package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class SetPlatformNotificationPriorityUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke(platformId: Int, notificationPriority: String) =
        platformRepository.setNotificationPriority(platformId, notificationPriority)
}
