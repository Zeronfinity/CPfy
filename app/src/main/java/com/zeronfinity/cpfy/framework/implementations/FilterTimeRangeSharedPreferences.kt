package com.zeronfinity.cpfy.framework.implementations

import android.app.Application
import android.content.Context
import com.zeronfinity.core.repository.FilterTimeRangeDataSource
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.common.DEFAULT_DAYS_INTERVAL
import com.zeronfinity.cpfy.common.DEFAULT_MAX_DURATION
import com.zeronfinity.cpfy.common.DEFAULT_MIN_DURATION
import java.util.Calendar
import java.util.Date

class FilterTimeRangeSharedPreferences(
    private val application: Application
) : FilterTimeRangeDataSource {
    private val sharedPref = application.getSharedPreferences(
        application.getString(R.string.shared_preferences_filters),
        Context.MODE_PRIVATE
    )
    private val minDefaultDuration = DEFAULT_MIN_DURATION
    private val maxDefaultDuration = DEFAULT_MAX_DURATION
    private val numberOfDaysBeforeContestsEnd = DEFAULT_DAYS_INTERVAL

    override fun isSaved(): Boolean {
        return sharedPref.getBoolean(
            application.getString(R.string.s_pref_filters_is_saved),
            false
        )
    }

    override fun setSaved(value: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(
                application.getString(R.string.s_pref_filters_is_saved),
                value
            )
            apply()
        }
    }

    override fun getStartTimeLowerBound(): Date {
        val date = sharedPref.getLong(
            application.getString(R.string.s_pref_filters_key_start_time_lower_bound),
            Date().time
        )

        return Date(date)
    }

    override fun getStartTimeUpperBound(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        val date = sharedPref.getLong(
            application.getString(R.string.s_pref_filters_key_start_time_upper_bound),
            calendar.timeInMillis
        )

        return Date(date)
    }

    override fun getEndTimeLowerBound(): Date {
        val date = sharedPref.getLong(
            application.getString(R.string.s_pref_filters_key_end_time_lower_bound),
            Date().time
        )

        return Date(date)
    }

    override fun getEndTimeUpperBound(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        val date = sharedPref.getLong(
            application.getString(R.string.s_pref_filters_key_end_time_upper_bound),
            calendar.timeInMillis
        )

        return Date(date)
    }

    override fun getDurationLowerBound(): Int {
        return sharedPref.getInt(
            application.getString(R.string.s_pref_filters_key_duration_lower_bound),
            minDefaultDuration
        )
    }

    override fun getDurationUpperBound(): Int {
        return sharedPref.getInt(
            application.getString(R.string.s_pref_filters_key_duration_upper_bound),
            maxDefaultDuration
        )
    }

    override fun setStartTimeLowerBound(date: Date) {
        with(sharedPref.edit()) {
            putLong(
                application.getString(R.string.s_pref_filters_key_start_time_lower_bound),
                date.time
            )
            apply()
        }
    }

    override fun setStartTimeUpperBound(date: Date) {
        with(sharedPref.edit()) {
            putLong(
                application.getString(R.string.s_pref_filters_key_start_time_upper_bound),
                date.time
            )
            apply()
        }
    }

    override fun setEndTimeLowerBound(date: Date) {
        with(sharedPref.edit()) {
            putLong(
                application.getString(R.string.s_pref_filters_key_end_time_lower_bound),
                date.time
            )
            apply()
        }
    }

    override fun setEndTimeUpperBound(date: Date) {
        with(sharedPref.edit()) {
            putLong(
                application.getString(R.string.s_pref_filters_key_end_time_upper_bound),
                date.time
            )
            apply()
        }
    }

    override fun setDurationLowerBound(duration: Int) {
        with(sharedPref.edit()) {
            putInt(
                application.getString(R.string.s_pref_filters_key_duration_lower_bound),
                duration
            )
            apply()
        }
    }

    override fun setDurationUpperBound(duration: Int) {
        with(sharedPref.edit()) {
            putInt(
                application.getString(R.string.s_pref_filters_key_duration_upper_bound),
                duration
            )
            apply()
        }
    }
}
