package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum

class SetFilterDurationUseCase(private val filterTimeRangeRepository: FilterTimeRangeRepository) {
    operator fun invoke(filterDurationEnum: FilterDurationEnum, duration: Int) = filterTimeRangeRepository.setFilterDuration(filterDurationEnum, duration)
}
