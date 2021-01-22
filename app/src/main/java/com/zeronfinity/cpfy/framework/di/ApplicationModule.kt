package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.core.repository.*
import com.zeronfinity.core.usecase.*
import com.zeronfinity.cpfy.CustomApplication
import com.zeronfinity.cpfy.framework.db.dao.ContestDao
import com.zeronfinity.cpfy.framework.db.dao.ContestNotificationDao
import com.zeronfinity.cpfy.framework.db.dao.PlatformDao
import com.zeronfinity.cpfy.framework.implementations.*
import com.zeronfinity.cpfy.framework.network.ClistNetworkCall
import com.zeronfinity.cpfy.framework.notification.NotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Singleton
    @Provides
    fun provideContestRepository(contestDao: ContestDao) =
        ContestRepository(ContestDb(contestDao))

    @Singleton
    @Provides
    fun provideContestNotificationRepository(contestNotificationDao: ContestNotificationDao) =
        ContestNotificationRepository(
            ContestNotificationDb(
                Dispatchers.IO, contestNotificationDao
            )
        )

    @Singleton
    @Provides
    fun provideFirstRunRepository(application: Application) =
        FirstRunRepository(
            FirstRunSharedPreferences(
                application
            )
        )

    @Singleton
    @Provides
    fun providePlatformRepository(platformDao: PlatformDao) = PlatformRepository(
        PlatformDb(
            Dispatchers.IO, platformDao
        )
    )

    @Singleton
    @Provides
    fun provideServerContestInfoRepository(clistNetworkCall: ClistNetworkCall) =
        ServerContestInfoRepository(ServerContestInfoClist(clistNetworkCall))

    @Singleton
    @Provides
    fun provideServerPlatformInfoRepository(
        application: Application,
        clistNetworkCall: ClistNetworkCall
    ) =
        ServerPlatformInfoRepository(
            ServerPlatformInfoClist(
                application as CustomApplication,
                clistNetworkCall
            )
        )

    @Singleton
    @Provides
    fun provideFilterTimeRangeRepository(application: Application) =
        FilterTimeRangeRepository(FilterTimeRangeSharedPreferences(application as CustomApplication))

    @Singleton
    @Provides
    fun provideCookieRepository(application: Application) =
        CookieRepository(CookieSharedPreferences(application as CustomApplication))

    @Provides
    fun provideFetchServerContestsUseCase(
        serverContestInfoRepository: ServerContestInfoRepository
    ) = FetchServerContestsUseCase(
        serverContestInfoRepository
    )

    @Provides
    fun provideGetContestUseCase(
        contestRepository: ContestRepository
    ) = GetContestUseCase(
        contestRepository
    )

    @Provides
    fun provideGetPlatformUseCase(
        platformRepository: PlatformRepository
    ) = GetPlatformUseCase(
        platformRepository
    )

    @Provides
    fun provideGetPlatformNotificationPriorityUseCase(
        platformRepository: PlatformRepository
    ) = GetPlatformNotificationPriorityUseCase(
        platformRepository
    )

    @Provides
    fun provideSetPlatformNotificationPriorityUseCase(
        platformRepository: PlatformRepository
    ) = SetPlatformNotificationPriorityUseCase(
        platformRepository
    )


    @Provides
    fun provideGetCookieUseCase(
        cookieRepository: CookieRepository
    ) = GetCookieUseCase(
        cookieRepository
    )

    @Provides
    fun provideClearNotificationContestsUseCase(
        contestNotificationRepository: ContestNotificationRepository
    ) = ClearNotificationContestsUseCase(
        contestNotificationRepository
    )

    @Provides
    fun provideGetNotificationContestsUseCase(
        contestNotificationRepository: ContestNotificationRepository
    ) = GetNotificationContestsUseCase(
        contestNotificationRepository
    )

    @Provides
    fun provideInsertNotificationContestsUseCase(
        contestNotificationRepository: ContestNotificationRepository
    ) = InsertNotificationContestsUseCase(
        contestNotificationRepository
    )

    @Provides
    fun provideNotificationHelper(
        application: Application
    ) = NotificationHelper(
        application
    )
}
