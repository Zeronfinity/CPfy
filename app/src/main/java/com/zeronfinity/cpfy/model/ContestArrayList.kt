package com.zeronfinity.cpfy.model

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.repository.ContestDataSource
import java.util.Collections

class ContestArrayList : ContestDataSource {
    private val contestArrayList = Collections.synchronizedList(ArrayList<Contest>())

    override fun add(contestList: List<Contest>) {
        contestArrayList.addAll(contestList)
    }

    override fun get(index: Int): Contest = contestArrayList[index]

    override fun getList(): List<Contest> {
        return ArrayList(contestArrayList)
    }

    override fun size() = contestArrayList.size

    override fun clear() = contestArrayList.clear()
}
