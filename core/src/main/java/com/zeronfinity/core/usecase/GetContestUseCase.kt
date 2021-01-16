package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ContestRepository

class GetContestUseCase(private val contestRepository: ContestRepository) {
    suspend operator fun invoke(id: Int) = contestRepository.getContest(id)
}
