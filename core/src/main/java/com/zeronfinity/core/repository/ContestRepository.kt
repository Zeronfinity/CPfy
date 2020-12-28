package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Contest

class ContestRepository(private val contestDataSource: ContestDataSource) {
    fun addContestList(contestList: List<Contest>) = contestDataSource.add(contestList)

    fun getContest(index: Int) = contestDataSource.get(index)

    fun getContestList() = contestDataSource.getList()

    fun getContestCount() = contestDataSource.size()

    fun removeAllContests() = contestDataSource.clear()
}
