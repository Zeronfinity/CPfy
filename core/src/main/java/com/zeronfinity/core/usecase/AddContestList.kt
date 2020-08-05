package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.repository.ContestRepository

class AddContestList(private val contestRepository: ContestRepository) {
    operator fun invoke(contestList: List<Contest>) = contestRepository.addContestList(contestList)
}
