package com.zeronfinity.core.repository

import com.zeronfinity.core.entity.Contest

interface ContestDataSource {
    fun add(contestList: List<Contest>)

    fun get(index: Int): Contest

    fun size(): Int

    fun clear()
}
