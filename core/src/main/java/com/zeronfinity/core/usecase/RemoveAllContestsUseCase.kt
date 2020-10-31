package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ContestRepository

class RemoveAllContestsUseCase(private val contestRepository: ContestRepository) {
    operator fun invoke() = contestRepository.removeAllContests()
}
