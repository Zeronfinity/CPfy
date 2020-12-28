package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum

class GetFilterTimeUseCase(private val filterTimeRangeRepository: FilterTimeRangeRepository) {
    operator fun invoke(filterTimeEnum: FilterTimeEnum) = filterTimeRangeRepository.getFilterTimeRange(filterTimeEnum)
}
