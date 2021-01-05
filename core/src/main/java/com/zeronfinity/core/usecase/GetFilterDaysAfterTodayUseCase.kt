package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum

class GetFilterDaysAfterTodayUseCase(private val filterTimeRangeRepository: FilterTimeRangeRepository) {
    operator fun invoke(filterTimeTypeEnum: FilterTimeTypeEnum) =
        filterTimeRangeRepository.getDaysAfterToday(filterTimeTypeEnum)
}
