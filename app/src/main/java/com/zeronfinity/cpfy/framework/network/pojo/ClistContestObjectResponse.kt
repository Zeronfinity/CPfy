package com.zeronfinity.cpfy.framework.network.pojo

import com.squareup.moshi.Json
import com.zeronfinity.core.entity.Contest
import java.text.SimpleDateFormat
import java.util.Locale

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
        val simpleDateFormatUtc = SimpleDateFormat(apiDateFormat, Locale.US)
        val duration = ((simpleDateFormatUtc.parse(end).time - simpleDateFormatUtc.parse(start).time)/1000).toInt()
        return Contest(
            title,
            duration,
            platformResourceObject.platformId,
            simpleDateFormatUtc.parse(start),
            simpleDateFormatUtc.parse(end),
            url
        )
    }
}
