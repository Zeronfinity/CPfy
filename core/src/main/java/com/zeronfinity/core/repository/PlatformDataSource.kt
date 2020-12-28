package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

interface PlatformDataSource {
    fun add(platform: Platform)

    fun add(platformList: List<Platform>)

    fun enablePlatform(name: String)

    fun disablePlatform(name: String)

    fun getImageUrl(name: String): String?

    fun getPlatform(name: String): Platform?

    fun getPlatformList(): List<Platform>

    fun isPlatformEnabled(name: String): Boolean

    fun removeAll()

    fun size(): Int
}
