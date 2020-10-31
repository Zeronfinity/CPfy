package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

class PlatformRepository(private val platformDataSource: PlatformDataSource) {
    fun addPlatform(platform: Platform) = platformDataSource.add(platform)

    fun addPlatformList(platformList: List<Platform>) = platformDataSource.add(platformList)

    fun getImageUrl(platformName: String) = platformDataSource.getImageUrl(platformName)

    fun removeAllPlatforms() = platformDataSource.removeAll()
}
