package com.zeronfinity.cpfy.model

import android.app.Application
import android.content.Context
import com.zeronfinity.core.repository.CookieDataSource
import com.zeronfinity.cpfy.R

class CookieSharedPreferences(
    application: Application
) : CookieDataSource {
    private val sharedPref = application.getSharedPreferences(
        application.getString(R.string.shared_preferences_cookies),
        Context.MODE_PRIVATE
    )

    override fun get(cookieTitle: String) =
        sharedPref.getString(
            cookieTitle,
            null
        )

    override fun set(cookieTitle: String, rawCookieString: String) {
        with(sharedPref.edit()) {
            putString(
                cookieTitle,
                rawCookieString
            )
            commit()
        }
    }
}
