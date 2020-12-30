package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.core.repository.*
import com.zeronfinity.core.usecase.GetCookieUseCase
import com.zeronfinity.cpfy.CustomApplication
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiClient
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiInterface
import com.zeronfinity.cpfy.model.ContestArrayList
import com.zeronfinity.cpfy.model.FilterTimeRangeSharedPreferences
import com.zeronfinity.cpfy.model.PlatformMap
import com.zeronfinity.cpfy.model.ServerContestInfoClist
import com.zeronfinity.cpfy.model.network.ClistNetworkCall
import com.zeronfinity.cpfy.model.CookieSharedPreferences
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
    fun provideClistClient(
        application: Application,
        getCookieUseCase: GetCookieUseCase
    ): RetrofitClistApiClient =
        RetrofitClistApiClient(application as CustomApplication, getCookieUseCase)

    @Singleton
    @Provides
    fun provideClistApi(retrofitClistApiClient: RetrofitClistApiClient): RetrofitClistApiInterface = retrofitClistApiClient.getClistApi()

    @Singleton
    @Provides
    fun provideClistNetworkCall(application: Application, apiInterface: RetrofitClistApiInterface) =
        ClistNetworkCall(application as CustomApplication, apiInterface)

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
