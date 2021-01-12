package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.Platform

class GetOrderedPlatformListUseCase(
    private val getFilteredContestListUseCase: GetFilteredContestListUseCase
) {
    suspend operator fun invoke(platformList: List<Platform>): List<Platform> {
        val positionMap = mutableMapOf<Int, Int>()

        val contestList = getFilteredContestListUseCase()

        var index = 0
        for (contest in contestList) {
            if (!positionMap.containsKey(contest.platformId)) {
                positionMap[contest.platformId] = index++
            }
        }

        return platformList.sortedWith { platform1, platform2 ->
            (positionMap[platform1.id] ?: contestList.size) - (positionMap[platform2.id]
                ?: contestList.size)
        }
    }
}
