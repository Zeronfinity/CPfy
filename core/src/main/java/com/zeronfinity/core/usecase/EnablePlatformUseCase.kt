package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.PlatformRepository

class EnablePlatformUseCase(private val platformRepository: PlatformRepository) {
    operator fun invoke(platformName: String) = platformRepository.enablePlatform(platformName)
}
