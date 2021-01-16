package com.zeronfinity.cpfy.framework.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contest_notification")
data class ContestNotificationEntity(
    @PrimaryKey val contestId: Int
) {
    companion object {
        fun fromContestId(id: Int) =
            ContestNotificationEntity(id)
    }
}

