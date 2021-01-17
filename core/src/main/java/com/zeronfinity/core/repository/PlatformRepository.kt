package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

class PlatformRepository(private val platformDataSource: PlatformDataSource) {
    fun addPlatformList(platformList: List<Platform>) = platformDataSource.add(platformList)

    fun disablePlatform(platformId: Int) = platformDataSource.disablePlatform(platformId)

    fun disableAllPlatforms() = platformDataSource.disableAllPlatforms()

    fun enablePlatform(platformId: Int) = platformDataSource.enablePlatform(platformId)

    fun enableAllPlatforms() = platformDataSource.enableAllPlatforms()

    suspend fun getPlatform(platformId: Int) = platformDataSource.getPlatform(platformId)

    fun getPlatformList() = platformDataSource.getPlatformList()

    suspend fun isPlatformEnabled(platformId: Int) =
        platformDataSource.isPlatformEnabled(platformId)

    suspend fun getNotificationPriority(platformId: Int) =
        platformDataSource.getNotificationPriority(platformId)

    fun setNotificationPriority(platformId: Int, notificationPriority: String) =
        platformDataSource.setNotificationPriority(
            platformId, notificationPriority
        )

    fun setAllNotificationPriority(notificationPriority: String) =
        platformDataSource.setAllNotificationPriority(
            notificationPriority
        )
}
