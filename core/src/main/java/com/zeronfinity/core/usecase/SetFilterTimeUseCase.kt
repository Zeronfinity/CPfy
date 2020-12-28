package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum
import java.util.Date

class SetFilterTimeUseCase(private val filterTimeRangeRepository: FilterTimeRangeRepository) {
    operator fun invoke(filterTimeEnum: FilterTimeEnum, date: Date) = filterTimeRangeRepository.setFilterTimeRange(filterTimeEnum, date)
}
