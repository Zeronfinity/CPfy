package com.zeronfinity.core.repository

interface FirstRunDataSource {
    fun isFirstRun(): Boolean

    fun setFirstRun(value: Boolean)
}
