package com.zeronfinity.cpfy.model

import android.app.Application
import android.content.Context
import com.zeronfinity.core.repository.FilterTimeRangeDataSource
import com.zeronfinity.cpfy.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FilterTimeRangeSharedPreference(
    private val application: Application
) : FilterTimeRangeDataSource {
    private val sharedPref = application.getSharedPreferences(
        application.getString(R.string.shared_preference_filters),
        Context.MODE_PRIVATE
    )
    private val simpleDateFormat = SimpleDateFormat(
        "dd-MM-yy HH:mm",
        Locale.getDefault()
    )

    override fun getStartTimeLowerBound(): Date {
        val dateString = sharedPref.getString(
            application.getString(R.string.s_pref_filters_key_start_time_lower_bound),
            null
        )

        return dateString?.let { simpleDateFormat.parse(dateString) } ?: Date()
    }

    override fun getStartTimeUpperBound(): Date {
        val dateString = sharedPref.getString(
            application.getString(R.string.s_pref_filters_key_start_time_upper_bound),
            null
        )

        return dateString?.let { simpleDateFormat.parse(dateString) } ?: Date()
    }

    override fun getEndTimeLowerBound(): Date {
        val dateString = sharedPref.getString(
            application.getString(R.string.s_pref_filters_key_end_time_lower_bound),
            null
        )

        return dateString?.let { simpleDateFormat.parse(dateString) } ?: Date()
    }

    override fun getEndTimeUpperBound(): Date {
        val dateString = sharedPref.getString(
            application.getString(R.string.s_pref_filters_key_end_time_upper_bound),
            null
        )

        return dateString?.let { simpleDateFormat.parse(dateString) } ?: Date()
    }

    override fun getDurationLowerBound(): Date {
        val dateString = sharedPref.getString(
            application.getString(R.string.s_pref_filters_key_duration_lower_bound),
            null
        )

        return dateString?.let { simpleDateFormat.parse(dateString) } ?: Date()
    }

    override fun getDurationUpperBound(): Date {
        val dateString = sharedPref.getString(
            application.getString(R.string.s_pref_filters_key_duration_upper_bound),
            null
        )

        return dateString?.let { simpleDateFormat.parse(dateString) } ?: Date()
    }

    override fun setStartTimeLowerBound(date: Date) {
        with(sharedPref.edit()) {
            putString(
                application.getString(R.string.s_pref_filters_key_start_time_lower_bound),
                simpleDateFormat.format(date)
            )
            apply()
        }
    }

    override fun setStartTimeUpperBound(date: Date) {
        with(sharedPref.edit()) {
            putString(
                application.getString(R.string.s_pref_filters_key_start_time_upper_bound),
                simpleDateFormat.format(date)
            )
            apply()
        }
    }

    override fun setEndTimeLowerBound(date: Date) {
        with(sharedPref.edit()) {
            putString(
                application.getString(R.string.s_pref_filters_key_end_time_lower_bound),
                simpleDateFormat.format(date)
            )
            apply()
        }
    }

    override fun setEndTimeUpperBound(date: Date) {
        with(sharedPref.edit()) {
            putString(
                application.getString(R.string.s_pref_filters_key_end_time_upper_bound),
                simpleDateFormat.format(date)
            )
            apply()
        }
    }

    override fun setDurationLowerBound(date: Date) {
        with(sharedPref.edit()) {
            putString(
                application.getString(R.string.s_pref_filters_key_duration_lower_bound),
                simpleDateFormat.format(date)
            )
            apply()
        }
    }

    override fun setDurationUpperBound(date: Date) {
        with(sharedPref.edit()) {
            putString(
                application.getString(R.string.s_pref_filters_key_duration_upper_bound),
                simpleDateFormat.format(date)
            )
            apply()
        }
    }
}
