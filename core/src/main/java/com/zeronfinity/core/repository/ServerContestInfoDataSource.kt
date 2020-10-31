package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.ServerContestInfoResponse

interface ServerContestInfoDataSource {
    suspend fun getInfo(params: Map<String, String>) : ServerContestInfoResponse
}