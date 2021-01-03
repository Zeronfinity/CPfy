package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.repository.PlatformRepository

class GetOrderedPlatformListUseCase(
    private val getFilteredContestListUseCase: GetFilteredContestListUseCase,
    private val platformRepository: PlatformRepository
) {
    suspend operator fun invoke(): List<Platform>? {
        val positionMap = mutableMapOf<Int, Int>()

        val platformList = platformRepository.getPlatformList()
        val contestList = getFilteredContestListUseCase()

        var index = 0
        for (contest in contestList) {
            if (!positionMap.containsKey(contest.platformId)) {
                positionMap[contest.platformId] = index++
            }
        }

        return platformList?.sortedWith(Comparator { platform1, platform2 ->
            (positionMap[platform1.id] ?: contestList.size) - (positionMap[platform2.id] ?: contestList.size)
        })
    }
}
