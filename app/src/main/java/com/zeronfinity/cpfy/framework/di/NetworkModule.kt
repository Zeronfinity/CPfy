package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiClient
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiInterface
import com.zeronfinity.cpfy.model.network.ClistNetworkCall
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideClistApi(): RetrofitClistApiInterface = RetrofitClistApiClient.getClistApi()

    @Singleton
    @Provides
    fun provideClistNetworkCall(application: Application) = ClistNetworkCall(application)
}
