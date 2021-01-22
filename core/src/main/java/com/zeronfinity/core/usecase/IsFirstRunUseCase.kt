package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.FirstRunRepository

class IsFirstRunUseCase(private val firstRunRepository: FirstRunRepository) {
    operator fun invoke() = firstRunRepository.isFirstRun()
}
