package com.zeronfinity.cpfy.framework.di

import com.zeronfinity.core.repository.PlatformDataSource
import dagger.Module
import dagger.Provides

@Module
class PlatformDataSourceModule(private val platformDataSource: PlatformDataSource) {
    @Provides
    fun providePlatformDataSource() = platformDataSource
}
