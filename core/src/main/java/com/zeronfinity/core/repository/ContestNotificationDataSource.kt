package com.zeronfinity.core.repository

interface ContestNotificationDataSource {
    fun addContestIdList(contestIdList: List<Int>)

    suspend fun getContestIdList(): List<Int>?

    fun removeContestId(contestId: Int)

    fun clear()
}
