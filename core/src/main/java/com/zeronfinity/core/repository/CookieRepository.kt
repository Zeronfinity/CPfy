package com.zeronfinity.core.repository

class CookieRepository(private val cookieDataSource: CookieDataSource) {
    fun setCookie(cookieTitle: String, rawCookieString: String) = cookieDataSource.set(cookieTitle, rawCookieString)

    fun getCookie(cookieTitle: String) = cookieDataSource.get(cookieTitle)
}
