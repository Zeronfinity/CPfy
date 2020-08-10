package com.zeronfinity.cpfy.framework.di

import com.zeronfinity.core.repository.ContestDataSource
import dagger.Module
import dagger.Provides

@Module
class ContestDataSourceModule(private val contestDataSource: ContestDataSource) {
    @Provides
    fun provideContestDataSource() = contestDataSource
}
