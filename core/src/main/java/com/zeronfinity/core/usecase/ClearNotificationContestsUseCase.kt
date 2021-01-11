package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ContestNotificationRepository

class ClearNotificationContestsUseCase(private val contestNotificationRepository: ContestNotificationRepository) {
    operator fun invoke() = contestNotificationRepository.clear()
}
