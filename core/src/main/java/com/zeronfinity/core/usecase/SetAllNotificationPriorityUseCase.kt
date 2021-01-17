package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class SetAllNotificationPriorityUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke(notificationPriority: String) = platformRepository.setAllNotificationPriority(notificationPriority)
}
