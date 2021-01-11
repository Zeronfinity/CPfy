package com.zeronfinity.cpfy.framework.implementations

import com.zeronfinity.core.repository.ContestNotificationDataSource
import com.zeronfinity.cpfy.framework.db.dao.ContestNotificationDao
import com.zeronfinity.cpfy.framework.db.entity.ContestNotificationEntity.Companion.fromContestId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ContestNotificationDb(
    dispatcher: CoroutineDispatcher,
    private val contestNotificationDao: ContestNotificationDao
) : ContestNotificationDataSource {
    private val coroutineScope = CoroutineScope(dispatcher)

    private fun addContestId(contestId: Int) {
        coroutineScope.launch {
            contestNotificationDao.insert(fromContestId(contestId))
        }
    }

    override fun addContestIdList(contestIdList: List<Int>) {
        for (contestId in contestIdList) {
            addContestId(contestId)
        }
    }

    override suspend fun getContestIdList() = contestNotificationDao.getContestIdList()

    override fun removeContestId(contestId: Int) {
        coroutineScope.launch {
            contestNotificationDao.delete(contestId)
        }
    }

    override fun clear() {
        coroutineScope.launch {
            contestNotificationDao.deleteAll()
        }
    }
}
