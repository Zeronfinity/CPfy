package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Contest

class ContestRepository(private val contestDataSource: ContestDataSource) {
    suspend fun addContestList(contestList: List<Contest>) = contestDataSource.add(contestList)

    suspend fun getContest(id: Int) = contestDataSource.get(id)

    suspend fun getContestList() = contestDataSource.getList()

    fun getContestListFlow() = contestDataSource.getListFlow()

    suspend fun removeAllContests() = contestDataSource.clear()
}
