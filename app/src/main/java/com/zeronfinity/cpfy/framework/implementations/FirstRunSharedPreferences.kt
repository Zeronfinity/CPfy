package com.zeronfinity.cpfy.framework.implementations

import android.app.Application
import android.content.Context
import com.zeronfinity.core.repository.FirstRunDataSource
import com.zeronfinity.cpfy.R

class FirstRunSharedPreferences(
    private val application: Application
) : FirstRunDataSource {
    private val sharedPref = application.getSharedPreferences(
        application.getString(R.string.shared_preferences_first_run),
        Context.MODE_PRIVATE
    )

    override fun isFirstRun(): Boolean {
        return sharedPref.getBoolean(
            application.getString(R.string.s_pref_is_first_run),
            true
        )
    }

    override fun setFirstRun(value: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(
                application.getString(R.string.s_pref_is_first_run),
                value
            )
            apply()
        }
    }
}
