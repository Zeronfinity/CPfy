package com.zeronfinity.core.usecase

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.ContestRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.END_TIME
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.START_TIME
import com.zeronfinity.core.repository.PlatformRepository
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class GetFilteredContestListUseCase(
    private val contestRepository: ContestRepository,
    private val platformRepository: PlatformRepository,
    private val filterTimeRepository: FilterTimeRangeRepository
) {
    suspend operator fun invoke(): List<Contest> {
        val filteredContestList = ArrayList<Contest>()
        val allContestList = contestRepository.getContestList()

        var startTimeLowerBound = filterTimeRepository.getFilterTimeRange(START_TIME_LOWER_BOUND)
        var startTimeUpperBound = filterTimeRepository.getFilterTimeRange(START_TIME_UPPER_BOUND)
        var endTimeLowerBound = filterTimeRepository.getFilterTimeRange(END_TIME_LOWER_BOUND)
        var endTimeUpperBound = filterTimeRepository.getFilterTimeRange(END_TIME_UPPER_BOUND)
        val durationLowerBound = filterTimeRepository.getFilterDuration(DURATION_LOWER_BOUND)
        val durationUpperBound = filterTimeRepository.getFilterDuration(DURATION_UPPER_BOUND)

        if (filterTimeRepository.isLowerBoundToday(START_TIME)) {
            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            startTimeLowerBound = calendar.time

            calendar.add(Calendar.DAY_OF_YEAR, filterTimeRepository.getDaysAfterToday(START_TIME))
            startTimeUpperBound = calendar.time
        }

        if (filterTimeRepository.isLowerBoundToday(END_TIME)) {
            val calendar = GregorianCalendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            endTimeLowerBound = calendar.time

            calendar.add(Calendar.DAY_OF_YEAR, filterTimeRepository.getDaysAfterToday(END_TIME))
            endTimeUpperBound = calendar.time
        }

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
