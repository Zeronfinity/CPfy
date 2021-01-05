package com.zeronfinity.cpfy.framework.network.pojo

import com.squareup.moshi.Json
import com.zeronfinity.core.entity.Contest
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class ClistContestObjectResponse (
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
        val duration = ((simpleDateFormat.parse(end).time - simpleDateFormat.parse(start).time)/1000).toInt()
        return Contest(
            title,
            duration,
            platformResourceObject.platformId,
            simpleDateFormat.parse(start),
            simpleDateFormat.parse(end),
            url
        )
    }
}
