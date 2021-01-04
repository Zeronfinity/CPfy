package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FilterTimeRangeRepository

class IsFilterSavedUseCase(private val filterTimeRangeRepository: FilterTimeRangeRepository) {
    operator fun invoke() = filterTimeRangeRepository.isSaved()
}
