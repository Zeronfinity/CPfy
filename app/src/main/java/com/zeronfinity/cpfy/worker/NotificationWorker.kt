package com.zeronfinity.cpfy.worker

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.usecase.ClearNotificationContestsUseCase
import com.zeronfinity.core.usecase.FetchServerContestsUseCase
import com.zeronfinity.core.usecase.InsertNotificationContestsUseCase
import com.zeronfinity.cpfy.common.createNotificationAlarm
import com.zeronfinity.cpfy.framework.network.pojo.ClistContestObjectResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NotificationWorker @WorkerInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val fetchServerContestsUseCase: FetchServerContestsUseCase,
    private val clearNotificationContestsUseCase: ClearNotificationContestsUseCase,
    private val insertNotificationContestsUseCase: InsertNotificationContestsUseCase
) : CoroutineWorker(context, workerParams) {
    private val numberOfDaysForContestStartTime = 2

    override suspend fun doWork(): Result {
        logD("doWork() started")

        val simpleDateFormatUtc =
            SimpleDateFormat(ClistContestObjectResponse.apiDateFormat, Locale.US)
        simpleDateFormatUtc.timeZone = TimeZone.getTimeZone("UTC")

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysForContestStartTime)

        return withContext(Dispatchers.IO) {
            try {
                val contestList = fetchServerContestsUseCase(
                    mapOf(
                        "start__gte" to simpleDateFormatUtc.format(Date()),
                        "start__lte" to simpleDateFormatUtc.format(calendar.time),
                        "order_by" to "start"
                    )
                )

                logD("doWork() -> contestList[$contestList]")

                contestList?.let {
                    if (it.isNotEmpty()) {
                        val contestIdList = mutableListOf<Int>()
                        for (contest in it) {
                            createNotificationAlarm(context, contest)
                            contestIdList.add(contest.id)
                        }
                        clearNotificationContestsUseCase()
                        insertNotificationContestsUseCase(contestIdList)
                    } else {
                        return@withContext Result.failure()
                    }
                    return@withContext Result.success()
                }
                return@withContext Result.retry()
            } catch (ex: Exception) {
                return@withContext Result.retry()
            }
        }
    }
}
