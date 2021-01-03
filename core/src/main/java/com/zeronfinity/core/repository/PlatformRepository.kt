package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

class PlatformRepository(private val platformDataSource: PlatformDataSource) {
    fun addPlatformList(platformList: List<Platform>) = platformDataSource.add(platformList)

    fun disablePlatform(platformId: Int) = platformDataSource.disablePlatform(platformId)

    fun disableAllPlatforms() = platformDataSource.disableAllPlatforms()

    fun enablePlatform(platformId: Int) = platformDataSource.enablePlatform(platformId)

    fun enableAllPlatforms() = platformDataSource.enableAllPlatforms()

    suspend fun getPlatform(platformId: Int) = platformDataSource.getPlatform(platformId)

    suspend fun getPlatformList() = platformDataSource.getPlatformList()

    suspend fun isPlatformEnabled(platformId: Int) = platformDataSource.isPlatformEnabled(platformId)

    suspend fun getImageUrl(platformId: Int) = platformDataSource.getImageUrl(platformId)
}
