package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

interface PlatformDataSource {
    fun add(platform: Platform)

    fun add(platformList: List<Platform>)

    fun disablePlatform(id: Int)

    fun disableAllPlatforms()

    fun enablePlatform(id: Int)

    fun enableAllPlatforms()

    suspend fun getImageUrl(id: Int): String?

    suspend fun getPlatform(id: Int): Platform?

    suspend fun getPlatformList(): List<Platform>?

    suspend fun isPlatformEnabled(id: Int): Boolean?

    fun removeAll()

    suspend fun size(): Int?
}
