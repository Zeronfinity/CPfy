package com.zeronfinity.cpfy.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.usecase.GetContestUseCase
import com.zeronfinity.core.usecase.GetPlatformUseCase
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

    @Inject lateinit var getContestUseCase: GetContestUseCase
    @Inject lateinit var getPlatformUseCase: GetPlatformUseCase
    @Inject lateinit var notificationHelper: NotificationHelper


    override fun onReceive(context: Context?, intent: Intent?) {
        val contestId = intent?.getIntExtra("contestId", -1)

        logD("onReceive() -> contestId: [$contestId]")

        if (contestId == -1) return

        context?.let { ctx ->
            contestId?.let { contestId ->
                val contest = getContestUseCase(contestId)
                coroutineScope.launch {
                    val platform = getPlatformUseCase(contest.platformId)

                    platform?.let {
                        notificationHelper.createNotificationChannel(
                            "ch-${it.id}",
                            it.shortName,
                            "${it.shortName} notification channel"
                        )

                        val builder = NotificationCompat.Builder(ctx, "ch-${contest.platformId}")
                            .setContentTitle(contest.name)
                            .setContentText("Starts at ${simpleDateFormat.format(contest.startTime)}!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        val notificationManager = NotificationManagerCompat.from(ctx)

                        notificationManager.notify(contestId, builder.build())
                    }
                }
            }
        }
    }
}
