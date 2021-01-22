package com.zeronfinity.core.repository

class FirstRunRepository(private val firstRunDataSource: FirstRunDataSource) {
    fun isFirstRun() = firstRunDataSource.isFirstRun()

    fun setFirstRun(value: Boolean) = firstRunDataSource.setFirstRun(value)
}
