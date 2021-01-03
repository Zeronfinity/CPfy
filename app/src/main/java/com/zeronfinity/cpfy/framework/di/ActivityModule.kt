package com.zeronfinity.cpfy.framework.di

import com.zeronfinity.core.repository.*
import com.zeronfinity.core.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    fun provideDisablePlatformUseCase(
        platformRepository: PlatformRepository
    ) = DisablePlatformUseCase(
        platformRepository
    )

    @Provides
    fun provideAllDisablePlatformUseCase(
        platformRepository: PlatformRepository
    ) = DisableAllPlatformsUseCase(
        platformRepository
    )

    @Provides
    fun provideEnablePlatformUseCase(
        platformRepository: PlatformRepository
    ) = EnablePlatformUseCase(
        platformRepository
    )

    @Provides
    fun provideEnableAllPlatformsUseCase(
        platformRepository: PlatformRepository
    ) = EnableAllPlatformsUseCase(
        platformRepository
    )

    @Provides
    fun provideIsPlatformEnabledUseCase(
        platformRepository: PlatformRepository
    ) = IsPlatformEnabledUseCase(
        platformRepository
    )

    @Provides
    fun provideGetFilteredContestListUseCase(
        contestRepository: ContestRepository,
        platformRepository: PlatformRepository,
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = GetFilteredContestListUseCase(
        contestRepository,
        platformRepository,
        filterTimeRangeRepository
    )
    
    @Provides
    fun provideGetPlatformUseCase(
        platformRepository: PlatformRepository
    ) = GetPlatformUseCase(
        platformRepository
    )
    
    @Provides
    fun provideGetPlatformListUseCase(
        platformRepository: PlatformRepository
    ) = GetPlatformListUseCase(
        platformRepository
    )

    @Provides
    fun provideGetOrderedPlatformListUseCase(
        getFilteredContestListUseCase: GetFilteredContestListUseCase,
        platformRepository: PlatformRepository
    ) = GetOrderedPlatformListUseCase(
        getFilteredContestListUseCase,
        platformRepository
    )

    @Provides
    fun provideFetchServerContestInfoUseCase(
        contestRepository: ContestRepository,
        serverContestInfoRepository: ServerContestInfoRepository
    ) = FetchServerContestInfoUseCase(
        contestRepository,
        serverContestInfoRepository
    )

    @Provides
    fun provideFetchServerPlatformInfoUseCase(
        platformRepository: PlatformRepository,
        serverPlatformInfoRepository: ServerPlatformInfoRepository
    ) = FetchServerPlatformInfoUseCase(
        platformRepository,
        serverPlatformInfoRepository
    )
    
    @Provides
    fun provideGetFilterTimeUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = GetFilterTimeUseCase(
        filterTimeRangeRepository
    )

    @Provides
    fun provideSetFilterTimeUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = SetFilterTimeUseCase(
        filterTimeRangeRepository
    )
    
    @Provides
    fun provideGetFilterDurationUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = GetFilterDurationUseCase(
        filterTimeRangeRepository
    )
    
    @Provides
    fun provideSetFilterDurationUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = SetFilterDurationUseCase(
        filterTimeRangeRepository
    )

    @Provides
    fun provideSetCookieUseCase(
        cookieRepository: CookieRepository
    ) = SetCookieUseCase(
        cookieRepository
    )
}
