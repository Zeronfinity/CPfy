package com.zeronfinity.cpfy.common

fun makeDurationText(duration: Int): String {
    var remainingDuration = duration / 60

    val minutes = remainingDuration % 60
    remainingDuration /= 60

    val hours = remainingDuration % 24
    remainingDuration /= 24

    val days = remainingDuration

    var text = ""

    if (days != 0) {
        if (text.isNotEmpty()) {
            text += " "
        }
        text += "${days}d"
    }

    if (hours != 0) {
        if (text.isNotEmpty()) {
            text += " "
        }
        text += "${hours}h"
    }

    if (text.isNotEmpty()) {
        text += " "
    }
    text += "${minutes}m"

    return text
}
