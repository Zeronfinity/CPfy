package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum

class GetFilterDurationUseCase(private val filterTimeRangeRepository: FilterTimeRangeRepository) {
    operator fun invoke(filterDurationEnum: FilterDurationEnum) = filterTimeRangeRepository.getFilterDuration(filterDurationEnum)
}
