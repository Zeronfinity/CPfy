package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform
import kotlinx.coroutines.flow.Flow

interface PlatformDataSource {
    fun add(platform: Platform)

    fun add(platformList: List<Platform>)

    fun disablePlatform(id: Int)

    fun disableAllPlatforms()

    fun enablePlatform(id: Int)

    fun enableAllPlatforms()

    suspend fun getImageUrl(id: Int): String?

    suspend fun getPlatform(id: Int): Platform?

    fun getPlatformList(): Flow<List<Platform>>

    suspend fun isPlatformEnabled(id: Int): Boolean?

    suspend fun getNotificationPriority(id: Int): String?

    fun setNotificationPriority(id: Int, notificationPriority: String)

    fun setAllNotificationPriority(notificationPriority: String)

    fun removeAll()

    suspend fun size(): Int?
}
