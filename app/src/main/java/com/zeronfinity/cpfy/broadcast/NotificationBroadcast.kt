package com.zeronfinity.cpfy.broadcast

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.usecase.GetContestUseCase
import com.zeronfinity.core.usecase.GetPlatformUseCase
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.framework.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class NotificationBroadcast : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val simpleDateFormat = SimpleDateFormat(
        "hh:mm a",
        Locale.getDefault()
    )

    @Inject
    lateinit var getContestUseCase: GetContestUseCase
    @Inject
    lateinit var getPlatformUseCase: GetPlatformUseCase
    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        val contestId = intent?.getIntExtra("contestId", -1)

        logD("onReceive() -> contestId: [$contestId]")

        if (contestId == -1) return

        context?.let { ctx ->
            contestId?.let { contestId ->
                coroutineScope.launch {
                    getContestUseCase(contestId)?.let { contest ->
                        getPlatformUseCase(contest.platformId)?.let { platform ->
                            if (platform.notificationPriority != "None") {
                                notificationHelper.createNotificationChannel(
                                    "ch-${platform.id}",
                                    platform.shortName,
                                    "${platform.shortName} notification channel",
                                    platform.notificationPriority
                                )

                                val notificationIntent = PendingIntent.getActivity(
                                    context,
                                    contest.id,
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(contest.url)
                                    ),
                                    PendingIntent.FLAG_ONE_SHOT
                                )

                                val builder = NotificationCompat.Builder(ctx, "ch-${contest.platformId}")
                                    .setAutoCancel(true)
                                    .setContentTitle("${platform.shortName}: ${contest.name}")
                                    .setContentText("Starts at ${simpleDateFormat.format(contest.startTime)}!")
                                    .setContentIntent(notificationIntent)
                                    .setPriority(getPriorityValue(platform.notificationPriority))
                                    .setSmallIcon(R.drawable.ic_stat_cpfy)

                                val notificationManager = NotificationManagerCompat.from(ctx)

                                notificationManager.notify(contestId, builder.build())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getPriorityValue(notificationPriority: String): Int {
        return when (notificationPriority) {
            "Default" -> NotificationCompat.PRIORITY_DEFAULT
            "Max" -> NotificationCompat.PRIORITY_MAX
            "High" -> NotificationCompat.PRIORITY_HIGH
            "Low" -> NotificationCompat.PRIORITY_LOW
            "Min" -> NotificationCompat.PRIORITY_MIN
            else -> NotificationCompat.PRIORITY_DEFAULT
        }
    }
}
