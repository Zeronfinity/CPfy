package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FirstRunRepository

class SetFirstRunUseCase(private val firstRunRepository: FirstRunRepository) {
    operator fun invoke(value: Boolean) = firstRunRepository.setFirstRun(value)
}
