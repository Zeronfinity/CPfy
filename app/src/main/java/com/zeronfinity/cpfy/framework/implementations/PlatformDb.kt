package com.zeronfinity.cpfy.framework.implementations

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.PlatformDataSource
import com.zeronfinity.cpfy.framework.db.dao.PlatformDao
import com.zeronfinity.cpfy.framework.db.entity.PlatformEntity.Companion.fromPlatform
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
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
                platform.notificationPriority = it.notificationPriority
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
            platformDao.setEnabled(id, false)
        }
    }

    override fun disableAllPlatforms() {
        coroutineScope.launch {
            platformDao.setEnabledAll(false)
        }
    }

    override fun enablePlatform(id: Int) {
        coroutineScope.launch {
            platformDao.setEnabled(id, true)
        }
    }

    override fun enableAllPlatforms() {
        coroutineScope.launch {
            platformDao.setEnabledAll(true)
        }
    }

    override suspend fun getImageUrl(id: Int) = platformDao.getImageUrl(id)

    override suspend fun getPlatform(id: Int) =
        platformDao.getPlatform(id)?.toPlatform()

    override fun getPlatformList() =
        platformDao.getPlatformAll().map { list ->
            list.map {
                it.toPlatform()
            }
        }

    override suspend fun isPlatformEnabled(id: Int) = platformDao.isEnabled(id)

    override suspend fun getNotificationPriority(id: Int) = platformDao.getNotificationPriority(id)

    override fun setNotificationPriority(id: Int, notificationPriority: String) {
        logD("setNotificationPriority() -> id: [$id], notificationPriority: [$notificationPriority]")
        coroutineScope.launch {
            platformDao.setNotificationPriority(id, notificationPriority)
        }
    }

    override suspend fun size() = platformDao.getRowCount()

    override fun removeAll() {
        coroutineScope.launch {
            platformDao.deleteAll()
        }
    }
}
