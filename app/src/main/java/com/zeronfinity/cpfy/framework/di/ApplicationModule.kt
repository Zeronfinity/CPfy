package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.core.repository.ContestDataSource
import com.zeronfinity.core.repository.ContestRepository
import com.zeronfinity.core.repository.PlatformDataSource
import com.zeronfinity.core.repository.PlatformRepository
import com.zeronfinity.core.usecase.*
import com.zeronfinity.cpfy.model.UseCases
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val application: Application) {
    @Singleton
    @Provides
    fun provideApplication() = application

    @Provides
    fun provideUseCases(
        contestRepository: ContestRepository,
        platformRepository: PlatformRepository
    ) =
        UseCases(
            AddContestList(contestRepository),
            GetContest(contestRepository),
            GetContestCount(contestRepository),
            RemoveAllContests(contestRepository),
            AddPlatform(platformRepository),
            GetPlatformImageUrl(platformRepository),
            RemoveAllPlatforms(platformRepository)
        )

    @Provides
    fun provideContestRepository(contestDataSource: ContestDataSource) =
        ContestRepository(contestDataSource)

    @Provides
    fun providePlatformRepository(platformDataSource: PlatformDataSource) =
        PlatformRepository(platformDataSource)
}
