package com.zeronfinity.cpfy.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zeronfinity.cpfy.common.DATABASE_NAME
import com.zeronfinity.cpfy.framework.db.dao.ContestNotificationDao
import com.zeronfinity.cpfy.framework.db.dao.PlatformDao
import com.zeronfinity.cpfy.framework.db.entity.ContestNotificationEntity
import com.zeronfinity.cpfy.framework.db.entity.PlatformEntity

@Database(entities = [ContestNotificationEntity::class, PlatformEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contestNotificationDao(): ContestNotificationDao
    abstract fun platformDao(): PlatformDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `contest_notification` (`contestId` INTEGER NOT NULL, PRIMARY KEY(`contestId`))")
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).addMigrations(MIGRATION_1_2).build()
        }
    }
}
