package com.zeronfinity.cpfy.framework.implementations

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.repository.ContestDataSource
import com.zeronfinity.cpfy.framework.db.dao.ContestDao
import com.zeronfinity.cpfy.framework.db.entity.ContestEntity.Companion.fromContest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ContestDb(
    private val contestDao: ContestDao
) : ContestDataSource {

    suspend fun add(contest: Contest) {
        contestDao.insert(fromContest(contest))
    }

    override suspend fun add(contestList: List<Contest>) {
        for (contest in contestList) {
            add(contest)
        }
    }

    override suspend fun get(id: Int) = contestDao.getContest(id)?.toContest()

    override suspend fun getList() = contestDao.getContestAll()?.map {
        it.toContest()
    }

    override fun getListFlow() = contestDao.getContestAllFlow().map { list ->
        list.map {
            it.toContest()
        }
    }

    override suspend fun size() = contestDao.getRowCount()

    override suspend fun clear() {
        contestDao.deleteAll()
    }
}
