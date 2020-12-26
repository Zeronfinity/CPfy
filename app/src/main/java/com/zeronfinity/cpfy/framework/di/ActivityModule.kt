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
    fun provideGetContestCountUseCase(
        contestRepository: ContestRepository
    ) = GetContestCountUseCase(
        contestRepository
    )

    @ActivityScoped
    @Provides
    fun provideGetContestUseCase(
        contestRepository: ContestRepository
    ) = GetContestUseCase(
        contestRepository
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
}
