package com.zeronfinity.cpfy.framework.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationHelper(private val context: Context) {
    fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String,
        notificationImportance: String
    ) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, getImportanceValue(notificationImportance)).apply {
                description = channelDescription
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getImportanceValue(notificationImportance: String): Int {
        return when (notificationImportance) {
            "Default" -> NotificationManager.IMPORTANCE_DEFAULT
            "None" -> NotificationManager.IMPORTANCE_NONE
            "Max" -> NotificationManager.IMPORTANCE_MAX
            "High" -> NotificationManager.IMPORTANCE_HIGH
            "Low" -> NotificationManager.IMPORTANCE_LOW
            "Min" -> NotificationManager.IMPORTANCE_MIN
            else -> NotificationManager.IMPORTANCE_UNSPECIFIED
        }
    }
}
