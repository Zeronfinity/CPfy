package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.core.repository.*
import com.zeronfinity.core.usecase.*
import com.zeronfinity.cpfy.model.ServerContestInfoClist
import com.zeronfinity.cpfy.model.UseCases
import com.zeronfinity.cpfy.model.network.ClistNetworkCall
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
        platformRepository: PlatformRepository,
        serverContestInfoRepository: ServerContestInfoRepository
    ) =
        UseCases(
            AddContestListUseCase(contestRepository),
            GetContestUseCase(contestRepository),
            GetContestCountUseCase(contestRepository),
            RemoveAllContestsUseCase(contestRepository),
            AddPlatformUseCase(platformRepository),
            AddPlatformListUseCase(platformRepository),
            GetPlatformImageUrlUseCase(platformRepository),
            RemoveAllPlatformsUseCase(platformRepository),
            FetchServerContestInfoUseCase(serverContestInfoRepository)
        )

    @Provides
    fun provideContestRepository(contestDataSource: ContestDataSource) =
        ContestRepository(contestDataSource)

    @Provides
    fun providePlatformRepository(platformDataSource: PlatformDataSource) =
        PlatformRepository(platformDataSource)

    @Provides
    fun provideServerContestInfoRepository(clistNetworkCall: ClistNetworkCall) =
        ServerContestInfoRepository(ServerContestInfoClist(clistNetworkCall))
}
