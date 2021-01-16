package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.cpfy.framework.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(application: Application) = AppDatabase.getInstance(application)

    @Provides
    fun provideContestDao(appDatabase: AppDatabase) = appDatabase.contestDao()

    @Provides
    fun provideContestNotificationDao(appDatabase: AppDatabase) = appDatabase.contestNotificationDao()

    @Provides
    fun providePlatformDao(appDatabase: AppDatabase) = appDatabase.platformDao()
}
