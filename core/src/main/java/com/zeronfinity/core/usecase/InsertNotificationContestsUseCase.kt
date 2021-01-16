package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ContestNotificationRepository

class InsertNotificationContestsUseCase(private val contestNotificationRepository: ContestNotificationRepository) {
    operator fun invoke(contestIdList: List<Int>) = contestNotificationRepository.addContestIdList(contestIdList)
}
