package com.zeronfinity.cpfy

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.zeronfinity.cpfy.framework.logger.AndroidLoggingHandler
import com.zeronfinity.cpfy.worker.NotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class CustomApplication : Application(), Configuration.Provider {
    var isPlatformListFetched = false

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        AndroidLoggingHandler.setup()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWork = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork(
            "notificationWork",
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWork
        )
    }

    fun getClistApiKey() = BuildConfig.CLIST_API_KEY
}
