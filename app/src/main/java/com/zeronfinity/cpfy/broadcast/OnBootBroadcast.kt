package com.zeronfinity.cpfy.broadcast

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.usecase.GetContestUseCase
import com.zeronfinity.core.usecase.GetNotificationContestsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBootBroadcast(
    private val getContestUseCase: GetContestUseCase,
    private val getNotificationContestsUseCase: GetNotificationContestsUseCase
) : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val oneHourInMilliSeconds = 60 * 60 * 1000

    override fun onReceive(context: Context?, intent: Intent?) {
        logD("onReceive() started -> intent action: [${intent?.action}]")

        if(intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            coroutineScope.launch {
                val contestIdList = getNotificationContestsUseCase()

                contestIdList?.let { list ->
                    for (id in list) {
                        val contest = getContestUseCase(id)

                        val broadcastIntent = Intent(context, NotificationBroadcast::class.java)
                        broadcastIntent.putExtra("contestId", contest.id)

                        val pendingIntent = PendingIntent.getBroadcast(
                            context, contest.id, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            contest.startTime.time - oneHourInMilliSeconds,
                            pendingIntent
                        )
                    }
                }
            }
        }
    }
}
