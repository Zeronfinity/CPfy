package com.zeronfinity.cpfy

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.cpfy.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
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

const val LOG_TAG = "CPfyMainActivity"

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

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getHttpConnectionData(url: String) : String =
        withContext(Dispatchers.IO) {
            try {
                val urlConnection = URL(url).openConnection() as HttpURLConnection
                urlConnection.inputStream.bufferedReader().readText()
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

    private suspend fun parseJsonData(jsonData: String) : ArrayList<ContestData> =
        withContext(Dispatchers.Default) {
            try {
                val jsonArray = JSONObject(jsonData).getJSONArray("objects")
                val contestList = ArrayList<ContestData>()

                imageDownloadStarted.clear()

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    contestList.add(
                        ContestData(
                            item.getString("event"),
                            item.getInt("duration"),
                            item.getJSONObject("resource").getString("name"),
                            simpleDateFormatUtc.parse(item.getString("start")),
                            item.getString("href")
                        )
                    )

                    val resourceItem = item.getJSONObject("resource")
                    val key = resourceItem.getString("name")
                    if (!platformImages.containsKey(key) && !imageDownloadStarted.containsKey(key)) {
                        imageDownloadStarted[key] = true
                        CoroutineScope(Dispatchers.Main).launch {
                            var bitmapImage =
                                getBitmapFromURL("https://clist.by" + resourceItem.getString("icon"))
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

        simpleDateFormatUtc.timeZone = TimeZone.getTimeZone("UTC")

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        val urlString = getString(R.string.api_url) +
            "contest/?username=" + getString(R.string.api_username)  + "&api_key=" + BuildConfig.CLIST_API_KEY +
            "&end__gt=" + simpleDateFormatUtc.format(Date()) +
            "&end__lt=" + simpleDateFormatUtc.format(Date(calendar.timeInMillis)) +
            "&order_by=end"

        Log.i(LOG_TAG, "apiUrl = $urlString")

        launch(Dispatchers.Main) {
            val jsonData = getHttpConnectionData(urlString)

            if (jsonData.isEmpty()) {
                Toast.makeText(applicationContext,"Network failure!", Toast.LENGTH_SHORT).show()
            } else {
                val retList = parseJsonData(jsonData)
                if (retList.isNullOrEmpty()) {
                    Toast.makeText(applicationContext,"JSON parsing failed!", Toast.LENGTH_SHORT).show()
                } else {
                    contestList.clear()
                    contestList.addAll(retList)
                    Log.i(LOG_TAG, contestList.toString())
                    binding.rvMainActivity.adapter!!.notifyDataSetChanged()
                }
            }
        }

        binding.rvMainActivity.adapter = AdapterContestList(contestList)
        binding.rvMainActivity.layoutManager = LinearLayoutManager(this)
        binding.rvMainActivity.setHasFixedSize(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
