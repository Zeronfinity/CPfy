package com.zeronfinity.cpfy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.cpfy.Model.ClistServerResponse
import com.zeronfinity.cpfy.Network.getContestData
import com.zeronfinity.cpfy.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

fun parseSecondsToString(durationInSeconds: Int) : String {
    val minutes = (durationInSeconds / 60) % 60
    val hours = (durationInSeconds / 60 / 60) % 60
    val days = durationInSeconds / 60 / 60 / 24
    var ret = ""
    if (days != 0) {
        ret += "$days day"
        if (days > 1) ret += "s"
    }
    if (hours != 0) {
        if (ret.isNotEmpty()) ret += " "
        ret += "$hours hour"
        if (hours > 1) ret += "s"
    }
    if (minutes != 0) {
        if (ret.isNotEmpty()) ret += " "
        ret += "$minutes minute"
        if (minutes > 1) ret += "s"
    }
    return ret
}

const val LOG_TAG = "CpfyMainActivity"

class MainActivity: AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val apiDateFormat = "yyyy-MM-dd'T'HH:mm:ss"
    private val numberOfDaysBeforeContestsEnd = 7
    private val simpleDateFormatUtc = SimpleDateFormat(apiDateFormat, Locale.US)

    private lateinit var binding: ActivityMainBinding
    private lateinit var job: Job

    data class ContestData(val name: String, val duration: Int,
                           val platformName: String, val startTime: Date, val url: String)
    private val contestList = ArrayList<ContestData>()

    private suspend fun processFetchedData(clistServerResponse: ClistServerResponse) : ArrayList<ContestData> =
        withContext(Dispatchers.Default) {
            try {
                val list = clistServerResponse.contestList
                val contestList = ArrayList<ContestData>()

                imageDownloadStarted.clear()

                for (i in list.indices) {
                    val contest = list[i]
                    val duration = (simpleDateFormatUtc.parse(contest.end).time - simpleDateFormatUtc.parse(contest.start).time)/1000
                    contestList.add(
                        ContestData(
                            contest.title,
                            duration.toInt(),
                            contest.platformResource.platformName,
                            simpleDateFormatUtc.parse(contest.start),
                            contest.url
                        )
                    )

                    val key = contest.platformResource.platformName
                    if (!platformImages.containsKey(key) && !imageDownloadStarted.containsKey(key)) {
                        imageDownloadStarted[key] = true
                        CoroutineScope(Dispatchers.Main).launch {
                            var bitmapImage =
                                getBitmapFromURL("https://clist.by" + contest.platformResource.iconUrlSegment)
                            if (bitmapImage != null) {
                                bitmapImage = getResizedBitmap(bitmapImage, 48, 48)
                                platformImages[key] = bitmapImage
                                binding.rvMainActivity.adapter!!.notifyDataSetChanged()
                            }
                        }
                    }
                }

                contestList
            } catch (e: Exception) {
                e.printStackTrace()
                ArrayList<ContestData>()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        binding.rvMainActivity.adapter = AdapterContestList(contestList)
        binding.rvMainActivity.layoutManager = LinearLayoutManager(this)
        binding.rvMainActivity.setHasFixedSize(true)

        simpleDateFormatUtc.timeZone = TimeZone.getTimeZone("UTC")

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        launch(Dispatchers.Main) {
            val clistServerResponse = getContestData(applicationContext,
                                                                        mapOf("end__gt" to simpleDateFormatUtc.format(Date()),
                                                                            "end__lt" to simpleDateFormatUtc.format(Date(calendar.timeInMillis)),
                                                                            "order_by" to "end"
                                                                        ))
            Log.i(LOG_TAG, clistServerResponse.toString())

            if (clistServerResponse != null) {
                contestList.clear()
                contestList.addAll(processFetchedData(clistServerResponse))
                binding.rvMainActivity.adapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
