package com.zeronfinity.cpfy.framework.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.zeronfinity.cpfy.framework.db.entity.PlatformEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlatformDao : BaseDao<PlatformEntity> {
    @Query("DELETE FROM platform")
    suspend fun deleteAll()

    @Query("SELECT image_url FROM platform WHERE id = :id LIMIT 1")
    suspend fun getImageUrl(id: Int): String?

    @Query("SELECT * FROM platform WHERE id = :id LIMIT 1")
    suspend fun getPlatform(id: Int): PlatformEntity?

    @Query("SELECT * FROM platform")
    fun getPlatformAll(): Flow<List<PlatformEntity>>

    @Query("SELECT COUNT(id) FROM platform")
    suspend fun getRowCount(): Int?

    @Query("SELECT is_enabled FROM platform WHERE id = :id LIMIT 1")
    suspend fun isEnabled(id: Int): Boolean?

    @Query("UPDATE platform SET is_enabled = :isEnabled WHERE id = :id")
    suspend fun setEnabled(id: Int, isEnabled: Boolean)

    @Query("UPDATE platform SET is_enabled = :isEnabled")
    suspend fun setEnabledAll(isEnabled: Boolean)

    @Query("SELECT notification_priority FROM platform WHERE id = :id LIMIT 1")
    suspend fun getNotificationPriority(id: Int): String?

    @Query("UPDATE platform SET notification_priority = :notificationPriority WHERE id = :id")
    suspend fun setNotificationPriority(id: Int, notificationPriority: String)

    @Query("UPDATE platform SET notification_priority = :notificationPriority")
    suspend fun setAllNotificationPriority(notificationPriority: String)
}
