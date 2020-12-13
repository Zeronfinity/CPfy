package com.zeronfinity.cpfy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    fun getClistApiKey() = BuildConfig.CLIST_API_KEY
}
