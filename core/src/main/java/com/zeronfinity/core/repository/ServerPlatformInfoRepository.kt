package com.zeronfinity.core.repository

class ServerPlatformInfoRepository(private val serverPlatformInfoDataSource: ServerPlatformInfoDataSource) {
    suspend fun getPlatformInfo() = serverPlatformInfoDataSource.getInfo()
}
