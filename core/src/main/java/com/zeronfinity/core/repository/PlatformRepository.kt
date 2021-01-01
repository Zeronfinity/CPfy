package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

class PlatformRepository(private val platformDataSource: PlatformDataSource) {
    fun addPlatformList(platformList: List<Platform>) = platformDataSource.add(platformList)

    fun enablePlatform(platformId: Int) = platformDataSource.enablePlatform(platformId)

    fun disablePlatform(platformId: Int) = platformDataSource.disablePlatform(platformId)

    fun getPlatform(platformId: Int) = platformDataSource.getPlatform(platformId)

    fun getPlatformList() = platformDataSource.getPlatformList()

    fun isPlatformEnabled(platformId: Int) = platformDataSource.isPlatformEnabled(platformId)

    fun getImageUrl(platformId: Int) = platformDataSource.getImageUrl(platformId)
}
