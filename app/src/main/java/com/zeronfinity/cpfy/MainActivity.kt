package com.zeronfinity.cpfy

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.coroutines.CoroutineContext

class MainActivity: AppCompatActivity(), CoroutineScope {
    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val contestApiUrl = "https://clist.by/api/v1/json/contest/"
    val apiUsername = "Zeronfinity"
    val apiKey = BuildConfig.CLIST_API_KEY
    val apiDateFormat = "yyyy-MM-dd'T'HH:mm:ss"
    val numberOfDaysForApiEndDate = 7

    data class ContestData(val name: String, val duration: Int, val platformName: String, val startTime: String, val endTime: String, val url: String)
    val contestList = ArrayList<ContestData>()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job = Job()

        val calendar = Calendar.getInstance()
        calendar.setTime(Date())
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysForApiEndDate)

        val urlString = contestApiUrl + "?username=" + apiUsername + "&api_key=" + apiKey +
            "&end__gt=" + SimpleDateFormat(apiDateFormat, Locale.getDefault()).format(Date()) +
            "&end__lt=" + SimpleDateFormat(apiDateFormat, Locale.getDefault()).format(Date(calendar.timeInMillis)) +
            "&order_by=end"

        launch(Dispatchers.Main) {
            val jsonData = getHttpConnectionData(urlString)

            if (jsonData.equals("")) {
                Toast.makeText(applicationContext,"Network failure!", Toast.LENGTH_SHORT).show()
            } else {
                launch(Dispatchers.Default) {
                    val jsonArray = JSONObject(jsonData).getJSONArray("objects")
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)

                        contestList.add(
                            ContestData(
                                item.getString("event"),
                                item.getInt("duration"),
                                item.getJSONObject("resource").getString("name"),
                                item.getString("start"),
                                item.getString("end"),
                                item.getString("href")
                            )
                        )
                    }
                    Log.i("contestList", contestList.toString())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
