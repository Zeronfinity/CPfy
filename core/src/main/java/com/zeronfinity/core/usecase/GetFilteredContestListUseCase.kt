package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.ContestRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.PlatformRepository

class GetFilteredContestListUseCase(
    private val contestRepository: ContestRepository,
    private val platformRepository: PlatformRepository,
    private val filterTimeRepository: FilterTimeRangeRepository
) {
    suspend operator fun invoke(): List<Contest> {
        val filteredContestList = ArrayList<Contest>()
        val allContestList = contestRepository.getContestList()

        val startTimeLowerBound = filterTimeRepository.getFilterTimeRange(START_TIME_LOWER_BOUND)
        val startTimeUpperBound = filterTimeRepository.getFilterTimeRange(START_TIME_UPPER_BOUND)
        val endTimeLowerBound = filterTimeRepository.getFilterTimeRange(END_TIME_LOWER_BOUND)
        val endTimeUpperBound = filterTimeRepository.getFilterTimeRange(END_TIME_UPPER_BOUND)
        val durationLowerBound = filterTimeRepository.getFilterDuration(DURATION_LOWER_BOUND)
        val durationUpperBound = filterTimeRepository.getFilterDuration(DURATION_UPPER_BOUND)

        logD("allContestList: [$allContestList]")
        logD(
            "startTimeLowerBound: [$startTimeLowerBound], startTimeUpperBound: [$startTimeUpperBound]" +
            ", endTimeLowerBound: [$endTimeLowerBound], endTimeUpperBound: [$endTimeUpperBound]" +
            ", durationLowerBound: [$durationLowerBound], durationUpperBound: [$durationUpperBound]"
        )

        for (contest in allContestList) {
            val isEnabled = platformRepository.isPlatformEnabled(contest.platformId)
            if (isEnabled != false &&
                contest.startTime in startTimeLowerBound..startTimeUpperBound &&
                contest.endTime in endTimeLowerBound..endTimeUpperBound &&
                contest.duration in durationLowerBound..durationUpperBound
            ) {
                filteredContestList.add(contest)
            }
        }

        logD("filteredContestList: [$filteredContestList]")

        return filteredContestList
    }
}
