package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.core.usecase.GetCookieUseCase
import com.zeronfinity.cpfy.CustomApplication
import com.zeronfinity.cpfy.framework.network.ClistNetworkCall
import com.zeronfinity.cpfy.framework.network.retrofit.RetrofitClistApiClient
import com.zeronfinity.cpfy.framework.network.retrofit.RetrofitClistApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
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
}
