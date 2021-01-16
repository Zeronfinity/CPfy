package com.zeronfinity.cpfy.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.usecase.GetContestUseCase
import com.zeronfinity.core.usecase.GetNotificationContestsUseCase
import com.zeronfinity.cpfy.common.createNotificationAlarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnBootBroadcast : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var getContestUseCase: GetContestUseCase
    @Inject
    lateinit var getNotificationContestsUseCase: GetNotificationContestsUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        logD("onReceive() started -> intent action: [${intent?.action}]")

        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            context?.let { ctx ->
                logD("onReceive() started -> intent action matched")

                coroutineScope.launch {
                    logD("onReceive() started -> coroutine started")

                    val contestIdList = getNotificationContestsUseCase()

                    logD("onReceive() -> contestIdList: [$contestIdList]")

                    contestIdList?.let { list ->
                        for (id in list) {
                            getContestUseCase(id)?.let { contest ->
                                createNotificationAlarm(ctx, contest)
                            }
                        }
                    }
                }
            }
        }
    }
}
