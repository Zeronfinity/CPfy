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
    private val filterTimeRangeRepository: FilterTimeRangeRepository
) {
    operator fun invoke(): List<Contest> {
        val filteredContestList = ArrayList<Contest>()
        val allContestList = contestRepository.getContestList()

        val startTimeLowerBound = filterTimeRangeRepository.getFilterTimeRange(START_TIME_LOWER_BOUND)
        val startTimeUpperBound = filterTimeRangeRepository.getFilterTimeRange(START_TIME_UPPER_BOUND)
        val endTimeLowerBound = filterTimeRangeRepository.getFilterTimeRange(END_TIME_LOWER_BOUND)
        val endTimeUpperBound = filterTimeRangeRepository.getFilterTimeRange(END_TIME_UPPER_BOUND)
        val durationLowerBound = filterTimeRangeRepository.getFilterDuration(DURATION_LOWER_BOUND)
        val durationUpperBound = filterTimeRangeRepository.getFilterDuration(DURATION_UPPER_BOUND)

        logD("allContestList: [$allContestList]")
        logD("startTimeLowerBound: [$startTimeLowerBound], startTimeUpperBound: [$startTimeUpperBound]" +
            ", endTimeLowerBound: [$endTimeLowerBound], endTimeUpperBound: [$endTimeUpperBound]" +
            ", durationLowerBound: [$durationLowerBound], durationUpperBound: [$durationUpperBound]")

        for (contest in allContestList) {
            if (platformRepository.isPlatformEnabled(contest.platformName) &&
                contest.startTime >= startTimeLowerBound &&
                contest.startTime <= startTimeUpperBound &&
                contest.endTime >= endTimeLowerBound &&
                contest.endTime <= endTimeUpperBound &&
                contest.duration >= durationLowerBound &&
                contest.duration <= durationUpperBound
            ) {
                filteredContestList.add(contest)
            }
        }

        logD("filteredContestList: [$filteredContestList]")

        return filteredContestList
    }
}
