package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FilterTimeRangeRepository

class SetFilterSavedUseCase(private val filterTimeRangeRepository: FilterTimeRangeRepository) {
    operator fun invoke(value: Boolean) = filterTimeRangeRepository.setSaved(value)
}
