package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

interface PlatformDataSource {
    fun add(platform: Platform)

    fun add(platformList: List<Platform>)

    fun getImageUrl(name: String): String?

    fun isPlatformEnabled(name: String): Boolean

    fun enablePlatform(name: String)

    fun disablePlatform(name: String)

    fun getPlatformList(): List<Platform>

    fun size(): Int

    fun removeAll()
}
