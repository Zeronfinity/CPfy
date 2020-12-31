package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.ServerPlatformInfoResponse

interface ServerPlatformInfoDataSource {
    suspend fun getInfo() : ServerPlatformInfoResponse
}
