package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.core.repository.*
import com.zeronfinity.core.usecase.GetCookieUseCase
import com.zeronfinity.cpfy.CustomApplication
import com.zeronfinity.cpfy.framework.implementations.*
import com.zeronfinity.cpfy.framework.network.ClistNetworkCall
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {
    @Singleton
    @Provides
    fun provideContestRepository() = ContestRepository(ContestArrayList())

    @Singleton
    @Provides
    fun providePlatformRepository() = PlatformRepository(PlatformMap())

    @Singleton
    @Provides
    fun provideServerContestInfoRepository(clistNetworkCall: ClistNetworkCall) =
        ServerContestInfoRepository(ServerContestInfoClist(clistNetworkCall))

    @Singleton
    @Provides
    fun provideServerPlatformInfoRepository(application: Application, clistNetworkCall: ClistNetworkCall) =
        ServerPlatformInfoRepository(ServerPlatformInfoClist(application as CustomApplication, clistNetworkCall))

    @Singleton
    @Provides
    fun provideFilterTimeRangeRepository(application: Application) =
        FilterTimeRangeRepository(FilterTimeRangeSharedPreferences(application as CustomApplication))

    @Singleton
    @Provides
    fun provideCookieRepository(application: Application) =
        CookieRepository(CookieSharedPreferences(application as CustomApplication))

    @Provides
    fun provideGetCookieUseCase(
        cookieRepository: CookieRepository
    ) = GetCookieUseCase(
        cookieRepository
    )
}
