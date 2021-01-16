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
    dispatcher: CoroutineDispatcher,
    private val contestDao: ContestDao
) : ContestDataSource {
    private val coroutineScope = CoroutineScope(dispatcher)

    fun add(contest: Contest) {
        coroutineScope.launch {
            contestDao.insert(fromContest(contest))
        }
    }

    override fun add(contestList: List<Contest>) {
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

    override fun clear() {
        coroutineScope.launch {
            contestDao.deleteAll()
        }
    }
}
