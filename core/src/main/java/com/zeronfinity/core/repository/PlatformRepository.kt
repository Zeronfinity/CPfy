package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

class PlatformRepository(private val platformDataSource: PlatformDataSource) {
    fun addPlatformList(platformList: List<Platform>) = platformDataSource.add(platformList)

    fun enablePlatform(name: String) = platformDataSource.enablePlatform(name)

    fun disablePlatform(name: String) = platformDataSource.disablePlatform(name)

    fun getPlatform(platformName: String) = platformDataSource.getPlatform(platformName)

    fun getPlatformList() = platformDataSource.getPlatformList()

    fun isPlatformEnabled(name: String) = platformDataSource.isPlatformEnabled(name)

    fun getImageUrl(platformName: String) = platformDataSource.getImageUrl(platformName)
}
