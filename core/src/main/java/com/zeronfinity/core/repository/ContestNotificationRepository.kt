package com.zeronfinity.core.repository

class ContestNotificationRepository(private val contestNotificationDataSource: ContestNotificationDataSource) {
    fun addContestIdList(contestIdList: List<Int>) = contestNotificationDataSource.addContestIdList(contestIdList)

    suspend fun getContestIdList() = contestNotificationDataSource.getContestIdList()

    fun removeContestId(contestId: Int) = contestNotificationDataSource.removeContestId(contestId)

    fun clear() = contestNotificationDataSource.clear()
}
