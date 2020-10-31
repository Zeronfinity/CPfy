package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Platform

interface PlatformDataSource {
    fun add(platform: Platform)

    fun add(platformList: List<Platform>)

    fun getImageUrl(name: String): String?

    fun removeAll()
}
