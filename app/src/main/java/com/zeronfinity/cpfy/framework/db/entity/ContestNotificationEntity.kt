package com.zeronfinity.cpfy.framework.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zeronfinity.core.entity.Platform

@Entity(tableName = "contest_notification")
data class ContestNotificationEntity(
    @PrimaryKey val contestId: Int
) {
    companion object {
        fun fromContestId(id: Int) =
            ContestNotificationEntity(id)
    }

    fun toContestId() = contestId
}

