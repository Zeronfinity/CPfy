package com.zeronfinity.cpfy

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

val platformImages = mutableMapOf<String, Bitmap?>()
val imageDownloadStarted = mutableMapOf<String, Boolean>()

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun getBitmapFromURL(url: String): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            BitmapFactory.decodeStream(urlConnection.inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    val matrix = Matrix()
    matrix.postScale(newWidth.toFloat() / bm.width, newHeight.toFloat() / bm.height)

    val resizedBitmap = Bitmap.createBitmap(
        bm, 0, 0, bm.width, bm.height, matrix, false
    )
    bm.recycle()
    return resizedBitmap
}

