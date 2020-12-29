package com.zeronfinity.core.repository

interface CookieDataSource {
    fun get(cookieTitle: String): String?

    fun set(cookieTitle: String, rawCookieString: String)
}
