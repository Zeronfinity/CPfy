package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum

class SetFilterLowerBoundTodayUseCase(private val filterTimeRangeRepository: FilterTimeRangeRepository) {
    operator fun invoke(
        filterTimeTypeEnum: FilterTimeTypeEnum,
        value: Boolean
    ) = filterTimeRangeRepository.setLowerBoundToday(filterTimeTypeEnum, value)
}
