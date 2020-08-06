package com.zeronfinity.cpfy.model

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.repository.PlatformDataSource

class PlatformMap : PlatformDataSource {
    companion object {
        private val platformImageUrlMap = mutableMapOf<String, String>()
    }

    override fun add(platform: Platform) {
        platformImageUrlMap[platform.name] = platform.imageUrl
    }

    override fun getImageUrl(name: String) = platformImageUrlMap[name]

    override fun removeAll() = platformImageUrlMap.clear()
}
