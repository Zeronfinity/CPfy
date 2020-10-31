package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ContestRepository

class GetContestCountUseCase(private val contestRepository: ContestRepository) {
    operator fun invoke() = contestRepository.getContestCount()
}
