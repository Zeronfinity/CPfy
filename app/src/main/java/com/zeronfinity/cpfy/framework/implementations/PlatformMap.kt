package com.zeronfinity.cpfy.framework.implementations

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.PlatformDataSource

class PlatformMap : PlatformDataSource {
    private val platformMap = mutableMapOf<Int, Platform>()

    override fun add(platform: Platform) {
        if (platformMap.containsKey(platform.id)) {
            platform.isEnabled = platformMap[platform.id]?.isEnabled ?: true
        }

        platformMap[platform.id] = platform
    }

    override fun add(platformList: List<Platform>) {
        for (platform in platformList) {
            add(platform)
        }
    }

    override fun enablePlatform(id: Int) {
        if (platformMap.containsKey(id)) {
            logD("enablePlatform() -> id: [$id]")
            platformMap[id]?.isEnabled = true
        }
    }

    override fun disablePlatform(id: Int) {
        if (platformMap.containsKey(id)) {
            logD("disablePlatform() -> id: [$id]")
            platformMap[id]?.isEnabled = false
        }
    }

    override fun getImageUrl(id: Int) = platformMap[id]?.imageUrl

    override fun getPlatform(id: Int): Platform? = platformMap[id]

    override fun getPlatformList() = ArrayList(platformMap.values)

    override fun isPlatformEnabled(id: Int): Boolean {
        return platformMap[id]?.isEnabled ?: true
    }

    override fun size() = platformMap.size

    override fun removeAll() = platformMap.clear()
}
