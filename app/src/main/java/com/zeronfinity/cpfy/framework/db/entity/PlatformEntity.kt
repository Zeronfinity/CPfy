package com.zeronfinity.cpfy.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.entity.Platform.Companion.createPlatformShortName

@Entity(tableName = "platform")
data class PlatformEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "is_enabled") var isEnabled: Boolean,
    @ColumnInfo(name = "notification_priority") var notificationPriority: String
) {
    companion object {
        fun fromPlatform(platform: Platform) =
            PlatformEntity(platform.id, platform.name, platform.imageUrl, platform.isEnabled, platform.notificationPriority)
    }

    fun toPlatform(): Platform {
        return Platform(id, name, imageUrl, createPlatformShortName(name), isEnabled, notificationPriority)
    }
}
