package com.zeronfinity.cpfy.framework

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.repository.PlatformDataSource

class PlatformMap : PlatformDataSource {
    private val platformImageUrlMap = mutableMapOf<String, String>()

    override fun add(platform: Platform) {
        platformImageUrlMap[platform.name] = platform.imageUrl
    }

    override fun getImageUrl(name: String) = platformImageUrlMap[name]

    override fun removeAll() = platformImageUrlMap.clear()
}
