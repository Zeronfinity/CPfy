package com.zeronfinity.cpfy.framework.di

import com.zeronfinity.core.repository.*
import com.zeronfinity.core.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.Cookie

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
    ) = GetOrderedPlatformListUseCase(
        getFilteredContestListUseCase
    )

    @Provides
    fun provideFetchServerContestInfoUseCase(
        contestRepository: ContestRepository,
        cookieRepository: CookieRepository,
        serverContestInfoRepository: ServerContestInfoRepository
    ) = FetchServerContestInfoUseCase(
        contestRepository,
        cookieRepository,
        serverContestInfoRepository
    )

    @Provides
    fun provideFetchServerPlatformInfoUseCase(
        cookieRepository: CookieRepository,
        platformRepository: PlatformRepository,
        serverPlatformInfoRepository: ServerPlatformInfoRepository
    ) = FetchServerPlatformInfoUseCase(
        cookieRepository,
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
    fun provideIsFilterSavedUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = IsFilterSavedUseCase(
        filterTimeRangeRepository
    )

    @Provides
    fun provideSetFilterSavedUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = SetFilterSavedUseCase(
        filterTimeRangeRepository
    )

    @Provides
    fun provideSetCookieUseCase(
        cookieRepository: CookieRepository
    ) = SetCookieUseCase(
        cookieRepository
    )
}
