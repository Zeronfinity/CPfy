package com.zeronfinity.cpfy

import android.app.Application
import com.zeronfinity.cpfy.framework.logger.AndroidLoggingHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application() {
    var isPlatformListFetched = false

    override fun onCreate() {
        super.onCreate()

        AndroidLoggingHandler.setup()
    }

    fun getClistApiKey() = BuildConfig.CLIST_API_KEY
}
