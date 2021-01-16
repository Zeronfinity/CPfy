package com.zeronfinity.cpfy.framework.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.zeronfinity.cpfy.framework.db.entity.ContestNotificationEntity

@Dao
interface ContestNotificationDao : BaseDao<ContestNotificationEntity> {
    @Query("DELETE FROM contest_notification WHERE contestId = :contestId")
    fun delete(contestId: Int)

    @Query("DELETE FROM contest_notification")
    fun deleteAll()

    @Query("SELECT contestId FROM contest_notification")
    suspend fun getContestIdList(): List<Int>?
}
