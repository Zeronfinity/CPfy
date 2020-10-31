package com.zeronfinity.core.repository

class ServerContestInfoRepository(private val serverContestInfoDataSource: ServerContestInfoDataSource) {
    suspend fun getContestInfo(params: Map<String, String>) = serverContestInfoDataSource.getInfo(params)
}