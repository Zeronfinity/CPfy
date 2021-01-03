package com.zeronfinity.cpfy.framework.implementations

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.PlatformDataSource
import com.zeronfinity.cpfy.framework.db.dao.PlatformDao
import com.zeronfinity.cpfy.framework.db.entity.PlatformEntity.Companion.fromPlatform
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PlatformDb(
    dispatcher: CoroutineDispatcher,
    private val platformDao: PlatformDao
) : PlatformDataSource {
    private val coroutineScope = CoroutineScope(dispatcher)

    override fun add(platform: Platform) {
        coroutineScope.launch {
            getPlatform(platform.id)?.let {
                platform.isEnabled = it.isEnabled
            }

            platformDao.insert(fromPlatform(platform))
        }
    }

    override fun add(platformList: List<Platform>) {
        for (platform in platformList) {
            add(platform)
        }
    }

    override fun disablePlatform(id: Int) {
        coroutineScope.launch {
            platformDao.setIsEnabled(id, false)
        }
    }

    override fun disableAllPlatforms() {
        coroutineScope.launch {
            platformDao.setAllIsEnabled(false)
        }
    }

    override fun enablePlatform(id: Int) {
        coroutineScope.launch {
            platformDao.setIsEnabled(id, true)
        }
    }

    override fun enableAllPlatforms() {
        coroutineScope.launch {
            platformDao.setAllIsEnabled(true)
        }
    }

    override suspend fun getImageUrl(id: Int) = platformDao.getImageUrl(id)

    override suspend fun getPlatform(id: Int) =
        platformDao.getPlatform(id)?.toPlatform()

    override suspend fun getPlatformList() =
        platformDao.getPlatformAll()?.map { it ->
            it.toPlatform()
        }

    override suspend fun isPlatformEnabled(id: Int) = platformDao.isEnabled(id)

    override suspend fun size() = platformDao.getRowCount()

    override fun removeAll() {
        coroutineScope.launch {
            platformDao.deleteAll()
        }
    }
}
