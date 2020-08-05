package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ContestRepository

class GetContest(private val contestRepository: ContestRepository) {
    operator fun invoke(id: Int) = contestRepository.getContest(id)
}
