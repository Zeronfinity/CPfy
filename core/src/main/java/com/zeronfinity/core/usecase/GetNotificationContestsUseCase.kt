package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ContestNotificationRepository

class GetNotificationContestsUseCase(private val contestNotificationRepository: ContestNotificationRepository) {
    suspend operator fun invoke() = contestNotificationRepository.getContestIdList()
}
