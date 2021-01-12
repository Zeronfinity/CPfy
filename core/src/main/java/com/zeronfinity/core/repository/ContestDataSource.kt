package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Contest
import kotlinx.coroutines.flow.Flow

interface ContestDataSource {
    fun add(contestList: List<Contest>)

    suspend fun get(id: Int): Contest?

    suspend fun getList(): List<Contest>?

    fun getListFlow(): Flow<List<Contest>>

    suspend fun size(): Int?

    fun clear()
}
