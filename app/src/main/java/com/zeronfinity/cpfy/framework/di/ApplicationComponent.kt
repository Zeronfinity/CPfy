package com.zeronfinity.cpfy.framework.di

import com.zeronfinity.cpfy.model.network.ClistNetworkCall
import com.zeronfinity.cpfy.view.MainActivity
import com.zeronfinity.cpfy.viewmodel.MainActivityViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    ContestDataSourceModule::class,
    NetworkModule::class,
    PlatformDataSourceModule::class
])
interface ApplicationComponent {
    fun inject(clistNetworkCall: ClistNetworkCall)
    fun inject(mainActivityViewModel: MainActivityViewModel)
    fun inject(mainActivity: MainActivity)
}
