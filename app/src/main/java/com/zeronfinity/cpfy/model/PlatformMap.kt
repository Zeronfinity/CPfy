package com.zeronfinity.cpfy.model

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.repository.PlatformDataSource

class PlatformMap : PlatformDataSource {
    private val platformMap = mutableMapOf<String, Platform>()

    override fun add(platform: Platform) {
        if (platformMap.containsKey(platform.name)) {
            platform.isEnabled = platformMap[platform.name]?.isEnabled ?: true
        }

        platformMap[platform.name] = platform
    }

    override fun add(platformList: List<Platform>) {
        for (platform in platformList) {
            add(platform)
        }
    }

    override fun getImageUrl(name: String) = platformMap[name]?.imageUrl

    override fun getPlatform(name: String): Platform? = platformMap[name]

    override fun isPlatformEnabled(name: String): Boolean =
        platformMap[name]?.isEnabled ?: true

    override fun enablePlatform(name: String) {
        if (platformMap.containsKey(name)) {
            platformMap[name]?.isEnabled = true
        }
    }

    override fun disablePlatform(name: String) {
        if (platformMap.containsKey(name)) {
            platformMap[name]?.isEnabled = false
        }
    }

    override fun getPlatformList() = ArrayList(platformMap.values)

    override fun size() = platformMap.size

    override fun removeAll() = platformMap.clear()
}
