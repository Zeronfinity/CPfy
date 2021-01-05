package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum

class IsFilterLowerBoundTodayUseCase(private val filterTimeRangeRepository: FilterTimeRangeRepository) {
    operator fun invoke(filterTimeTypeEnum: FilterTimeTypeEnum) =
        filterTimeRangeRepository.isLowerBoundToday(filterTimeTypeEnum)
}
