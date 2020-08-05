package com.zeronfinity.cpfy.framework

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.repository.ContestDataSource

class ContestArrayList : ContestDataSource {
    private val contestArrayList = ArrayList<Contest>()

    override fun add(contestList: List<Contest>) {
        contestArrayList.addAll(contestList)
    }

    override fun get(index: Int): Contest = contestArrayList[index]

    override fun size() = contestArrayList.size

    override fun clear() = contestArrayList.clear()
}