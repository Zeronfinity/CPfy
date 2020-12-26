package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

class PlatformRepository(private val platformDataSource: PlatformDataSource) {
    fun addPlatform(platform: Platform) = platformDataSource.add(platform)

    fun addPlatformList(platformList: List<Platform>) = platformDataSource.add(platformList)

    fun getImageUrl(platformName: String) = platformDataSource.getImageUrl(platformName)

    fun isPlatformEnabled(name: String) = platformDataSource.isPlatformEnabled(name)

    fun enablePlatform(name: String) = platformDataSource.enablePlatform(name)

    fun disablePlatform(name: String) = platformDataSource.disablePlatform(name)

    fun getPlatformList() = platformDataSource.getPlatformList()

    fun getPlatformCount() = platformDataSource.size()

    fun removeAllPlatforms() = platformDataSource.removeAll()
}
