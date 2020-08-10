package com.zeronfinity.cpfy.framework

import android.app.Application
import com.zeronfinity.cpfy.framework.di.*
import com.zeronfinity.cpfy.model.ContestArrayList
import com.zeronfinity.cpfy.model.PlatformMap

class SubApplication : Application() {
    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(
                ApplicationModule(
                    this
                )
            )
            .contestDataSourceModule(
                ContestDataSourceModule(
                    ContestArrayList()
                )
            )
            .platformDataSourceModule(
                PlatformDataSourceModule(
                    PlatformMap()
                )
            )
            .build()
    }

    fun getApplicationComponent() = applicationComponent
}
