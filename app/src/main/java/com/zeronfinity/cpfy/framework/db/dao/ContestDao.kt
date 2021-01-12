package com.zeronfinity.cpfy.framework.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.zeronfinity.cpfy.framework.db.entity.ContestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContestDao : BaseDao<ContestEntity> {
    @Query("DELETE FROM contest")
    suspend fun deleteAll()

    @Query("SELECT * FROM contest WHERE id = :id LIMIT 1")
    suspend fun getContest(id: Int): ContestEntity?

    @Query("SELECT * FROM contest ORDER BY start_time")
    suspend fun getContestAll(): List<ContestEntity>?

    @Query("SELECT * FROM contest ORDER BY start_time")
    fun getContestAllFlow(): Flow<List<ContestEntity>>

    @Query("SELECT COUNT(id) FROM contest")
    suspend fun getRowCount(): Int?
}
