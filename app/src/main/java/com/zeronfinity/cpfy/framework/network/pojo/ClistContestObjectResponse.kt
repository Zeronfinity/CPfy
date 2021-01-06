package com.zeronfinity.cpfy.framework.network.pojo

import com.squareup.moshi.Json
import com.zeronfinity.core.entity.Contest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class ClistContestObjectResponse (
    @field:Json(name = "id")
    var id: Int,
    @field:Json(name = "event")
    var title: String,
    @field:Json(name = "start")
    var start: String,
    @field:Json(name = "end")
    var end: String,
    @field:Json(name = "href")
    var url: String,
    @field:Json(name = "resource")
    var platformResourceObject: ClistResourceObjectResponse
) {
    companion object {
        const val apiDateFormat = "yyyy-MM-dd'T'HH:mm:ss"
    }

    fun toContest(): Contest {
        val simpleDateFormat = SimpleDateFormat(apiDateFormat, Locale.US)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        var duration: Int? = null
        simpleDateFormat.parse(end)?.let { end ->
            simpleDateFormat.parse(start)?.let { start ->
                duration = ((end.time - start.time)/1000).toInt()
            }
        }
        return Contest(
            id,
            title,
            duration ?: 0,
            platformResourceObject.platformId,
            simpleDateFormat.parse(start) ?: Date(0),
            simpleDateFormat.parse(end) ?: Date(0),
            url
        )
    }
}
