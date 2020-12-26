package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.repository.ContestRepository
import com.zeronfinity.core.repository.PlatformRepository

class GetFilteredContestListUseCase(
    private val contestRepository: ContestRepository,
    private val platformRepository: PlatformRepository
) {
    operator fun invoke(): List<Contest> {
        val filteredContestList = ArrayList<Contest>()
        val allContestList = contestRepository.getContestList()

        for (contest in allContestList) {
            if (platformRepository.isPlatformEnabled(contest.platformName)) {
                filteredContestList.add(contest)
            }
        }

        return filteredContestList
    }
}
