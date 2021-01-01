package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

interface PlatformDataSource {
    fun add(platform: Platform)

    fun add(platformList: List<Platform>)

    fun enablePlatform(id: Int)

    fun disablePlatform(id: Int)

    fun getImageUrl(id: Int): String?

    fun getPlatform(id: Int): Platform?

    fun getPlatformList(): List<Platform>

    fun isPlatformEnabled(id: Int): Boolean

    fun removeAll()

    fun size(): Int
}
