package com.zeronfinity.cpfy.model.network.pojo

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
    var platformResource: ClistResourceResponse
) {
    companion object {
        private const val apiDateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        val simpleDateFormatUtc = SimpleDateFormat(apiDateFormat, Locale.US)
        init {
            simpleDateFormatUtc.timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    fun toContest() = Contest(
        title,
        ((simpleDateFormatUtc.parse(end).time -
            simpleDateFormatUtc.parse(start).time)/1000).toInt(),
        platformResource.platformName,
        simpleDateFormatUtc.parse(start),
        simpleDateFormatUtc.parse(end),
        url
    )
}
