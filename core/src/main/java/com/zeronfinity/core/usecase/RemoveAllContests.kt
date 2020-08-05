package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.ContestRepository

class RemoveAllContests(private val contestRepository: ContestRepository) {
    operator fun invoke() = contestRepository.removeAllContests()
}
