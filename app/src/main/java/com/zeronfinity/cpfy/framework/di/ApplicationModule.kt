package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiClient
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiInterface
import com.zeronfinity.cpfy.model.network.ClistNetworkCall
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
    fun provideClistApi(): RetrofitClistApiInterface = RetrofitClistApiClient.getClistApi()

    @Singleton
    @Provides
    fun provideClistNetworkCall(application: Application, apiInterface: RetrofitClistApiInterface) =
        ClistNetworkCall(application, apiInterface)
}
