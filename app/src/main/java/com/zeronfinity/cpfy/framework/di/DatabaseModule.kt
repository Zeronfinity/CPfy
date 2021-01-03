package com.zeronfinity.cpfy.framework.di

import android.app.Application
import com.zeronfinity.cpfy.framework.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(application: Application) = AppDatabase.getInstance(application)

    @Provides
    fun providePlatformDao(appDatabase: AppDatabase) = appDatabase.platformDao()
}
