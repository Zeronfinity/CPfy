package com.zeronfinity.cpfy.framework.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T)
}
