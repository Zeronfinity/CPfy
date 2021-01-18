package com.zeronfinity.cpfy.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.broadcast.NotificationBroadcast

fun createNotificationAlarm(context: Context, contest: Contest) {
    context.logD("createNotificationAlarm() started -> contest: [$contest]")

    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, NotificationBroadcast::class.java)
    intent.putExtra("contestId", contest.id)

    val pendingIntent = PendingIntent.getBroadcast(context, contest.id, intent, FLAG_UPDATE_CURRENT)

    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        contest.startTime.time - NOTI_MILLI_SECONDS_BEFORE_CONTEST_STARTS,
        pendingIntent
    )
}

fun makeDurationText(durationInSeconds: Int): String {
    var remainingDuration = durationInSeconds / 60

    val minutes = remainingDuration % 60
    remainingDuration /= 60

    val hours = remainingDuration % 24
    remainingDuration /= 24

    val days = remainingDuration

    var text = ""

    if (days != 0) {
        if (text.isNotEmpty()) {
            text += " "
        }
        text += "${days}d"
    }

    if (hours != 0 || text.isNotEmpty()) {
        if (text.isNotEmpty()) {
            text += " "
        }
        text += "${hours}h"
    }

    if (text.isNotEmpty()) {
        text += " "
    }
    text += "${minutes}m"

    return text
}

fun makeFullDurationText(durationInSeconds: Long): String {
    var remainingDuration = durationInSeconds / 60

    val minutes = remainingDuration % 60
    remainingDuration /= 60

    val hours = remainingDuration % 24
    remainingDuration /= 24

    val days = remainingDuration

    var text = ""

    if (days != 0L) {
        if (text.isNotEmpty()) {
            text += " "
        }
        text += "$days day"
        if (days > 1) {
            text += "s"
        }
    }

    if (hours > 0) {
        if (text.isNotEmpty()) {
            text += " "
        }
        text += "$hours hour"
        if (hours > 1) {
            text += "s"
        }
    }

    if (minutes > 0 || text.isEmpty()) {
        if (text.isNotEmpty()) {
            text += " "
        }
        text += "$minutes minute"
        if (minutes > 1) {
            text += "s"
        }
    }

    return text
}
