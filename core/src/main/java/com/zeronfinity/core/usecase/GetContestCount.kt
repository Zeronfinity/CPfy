package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ContestRepository

class GetContestCount(private val contestRepository: ContestRepository) {
    operator fun invoke() = contestRepository.getContestCount()
}
