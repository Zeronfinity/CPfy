package com.zeronfinity.cpfy.framework.di

import com.zeronfinity.core.repository.*
import com.zeronfinity.core.usecase.*
import com.zeronfinity.cpfy.model.ServerContestInfoClist
import com.zeronfinity.cpfy.model.UseCases
import com.zeronfinity.cpfy.model.network.ClistNetworkCall
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
    fun provideUseCases(
        contestRepository: ContestRepository,
        platformRepository: PlatformRepository,
        serverContestInfoRepository: ServerContestInfoRepository
    ) = UseCases(
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

    @ActivityScoped
    @Provides
    fun provideServerContestInfoRepository(clistNetworkCall: ClistNetworkCall) =
        ServerContestInfoRepository(ServerContestInfoClist(clistNetworkCall))
}
