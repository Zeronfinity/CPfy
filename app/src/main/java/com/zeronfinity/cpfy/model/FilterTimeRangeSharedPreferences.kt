package com.zeronfinity.cpfy.model

import android.app.Application
import android.content.Context
import com.zeronfinity.core.repository.FilterTimeRangeDataSource
import com.zeronfinity.cpfy.R
import java.util.Calendar
import java.util.Date

class FilterTimeRangeSharedPreferences(
    private val application: Application
) : FilterTimeRangeDataSource {
    private val sharedPref = application.getSharedPreferences(
        application.getString(R.string.shared_preferences_filters),
        Context.MODE_PRIVATE
    )
    private val minDefaultDuration = 15 * 60
    private val maxDefaultDuration = 7 * 24 * 60 * 60
    private val numberOfDaysBeforeContestsEnd = 7

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
