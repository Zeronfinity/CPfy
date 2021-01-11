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
    /* platform use cases */

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

    /* filter use cases */

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
    fun provideFetchAndPersistServerContestsUseCase(
        contestRepository: ContestRepository,
        cookieRepository: CookieRepository,
        serverContestInfoRepository: ServerContestInfoRepository
    ) = FetchAndPersistServerContestsUseCase(
        contestRepository,
        cookieRepository,
        serverContestInfoRepository
    )

    @Provides
    fun provideFetchAndPersistServerPlatformsUseCase(
        cookieRepository: CookieRepository,
        platformRepository: PlatformRepository,
        serverPlatformInfoRepository: ServerPlatformInfoRepository
    ) = FetchAndPersistServerPlatformsUseCase(
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
    fun provideIsFilterLowerBoundTodayUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = IsFilterLowerBoundTodayUseCase(
        filterTimeRangeRepository
    )

    @Provides
    fun provideSetFilterLowerBoundTodayUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = SetFilterLowerBoundTodayUseCase(
        filterTimeRangeRepository
    )

    @Provides
    fun provideGetFilterDaysAfterTodayUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = GetFilterDaysAfterTodayUseCase(
        filterTimeRangeRepository
    )

    @Provides
    fun provideSetFilterDaysAfterTodayUseCase(
        filterTimeRangeRepository: FilterTimeRangeRepository
    ) = SetFilterDaysAfterTodayUseCase(
        filterTimeRangeRepository
    )

    /* cookie use cases */

    @Provides
    fun provideSetCookieUseCase(
        cookieRepository: CookieRepository
    ) = SetCookieUseCase(
        cookieRepository
    )
}
