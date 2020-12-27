package com.zeronfinity.cpfy.framework.di

import com.zeronfinity.core.repository.*
import com.zeronfinity.core.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @ActivityScoped
    @Provides
    fun provideDisablePlatformUseCase(
        platformRepository: PlatformRepository
    ) = DisablePlatformUseCase(
        platformRepository
    )

    @ActivityScoped
    @Provides
    fun provideEnablePlatformUseCase(
        platformRepository: PlatformRepository
    ) = EnablePlatformUseCase(
        platformRepository
    )

    @ActivityScoped
    @Provides
    fun provideIsPlatformEnabledUseCase(
        platformRepository: PlatformRepository
    ) = IsPlatformEnabledUseCase(
        platformRepository
    )

    @ActivityScoped
    @Provides
    fun provideGetFilteredContestListUseCase(
        contestRepository: ContestRepository,
        platformRepository: PlatformRepository
    ) = GetFilteredContestListUseCase(
        contestRepository,
        platformRepository
    )

    @ActivityScoped
    @Provides
    fun provideGetPlatformImageUrlUseCase(
        platformRepository: PlatformRepository
    ) = GetPlatformImageUrlUseCase(
        platformRepository
    )

    @ActivityScoped
    @Provides
    fun provideGetPlatformListUseCase(
        platformRepository: PlatformRepository
    ) = GetPlatformListUseCase(
        platformRepository
    )

    @ActivityScoped
    @Provides
    fun provideFetchServerContestInfoUseCase(
        contestRepository: ContestRepository,
        platformRepository: PlatformRepository,
        serverContestInfoRepository: ServerContestInfoRepository
    ) = FetchServerContestInfoUseCase(
        contestRepository,
        platformRepository,
        serverContestInfoRepository
    )

    @ActivityScoped
    @Provides
    fun provideGetFilterTimeUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = GetFilterTimeUseCase(
        filterTimeRangeRepository
    )

    @ActivityScoped
    @Provides
    fun provideSetFilterTimeUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = SetFilterTimeUseCase(
        filterTimeRangeRepository
    )
}
