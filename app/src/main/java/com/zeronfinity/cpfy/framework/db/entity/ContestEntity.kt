package com.zeronfinity.cpfy.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zeronfinity.core.entity.Contest
import java.util.Date

@Entity(tableName = "contest")
data class ContestEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "duration") val duration: Int,
    @ColumnInfo(name = "platform_id") val platformId: Int,
    @ColumnInfo(name = "start_time") val startTime: Long,
    @ColumnInfo(name = "end_time") val endTime: Long,
    @ColumnInfo(name = "url") val url: String
) {
    companion object {
        fun fromContest(contest: Contest) =
            ContestEntity(
                contest.id,
                contest.name,
                contest.duration,
                contest.platformId,
                contest.startTime.time,
                contest.endTime.time,
                contest.url
            )
    }

    fun toContest(): Contest {
        return Contest(id, name, duration, platformId, Date(startTime), Date(endTime), url)
    }
}
