package com.zeronfinity.cpfy.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.usecase.GetFilterTimeUseCase
import com.zeronfinity.core.usecase.SetFilterTimeUseCase
import java.text.SimpleDateFormat
import java.util.Locale

class FiltersViewModel @ViewModelInject constructor(
    private val getFilterTimeUseCase: GetFilterTimeUseCase,
    private val setFilterTimeUseCase: SetFilterTimeUseCase
) : ViewModel() {
    val startTimeLowerBoundLiveData = MutableLiveData<String>()
    val startTimeUpperBoundLiveData = MutableLiveData<String>()
    val endTimeLowerBoundLiveData = MutableLiveData<String>()
    val endTimeUpperBoundLiveData = MutableLiveData<String>()
    val durationLowerBoundLiveData = MutableLiveData<String>()
    val durationUpperBoundLiveData = MutableLiveData<String>()

    fun loadTimeFilterButtonTexts() {
        startTimeLowerBoundLiveData.postValue(
            SimpleDateFormat(
                "dd-MM-yy\nhh:mm a",
                Locale.getDefault()
            ).format(getFilterTimeUseCase(START_TIME_LOWER_BOUND))
        )

        startTimeUpperBoundLiveData.postValue(
            SimpleDateFormat(
                "dd-MM-yy\nhh:mm a",
                Locale.getDefault()
            ).format(getFilterTimeUseCase(START_TIME_UPPER_BOUND))
        )

        endTimeLowerBoundLiveData.postValue(
            SimpleDateFormat(
                "dd-MM-yy\nhh:mm a",
                Locale.getDefault()
            ).format(getFilterTimeUseCase(END_TIME_LOWER_BOUND))
        )

        endTimeUpperBoundLiveData.postValue(
            SimpleDateFormat(
                "dd-MM-yy\nhh:mm a",
                Locale.getDefault()
            ).format(getFilterTimeUseCase(END_TIME_UPPER_BOUND))
        )

        durationLowerBoundLiveData.postValue(
            SimpleDateFormat(
                "dd-MM-yy\nhh:mm a",
                Locale.getDefault()
            ).format(getFilterTimeUseCase(DURATION_LOWER_BOUND))
        )

        durationUpperBoundLiveData.postValue(
            SimpleDateFormat(
                "dd-MM-yy\nhh:mm a",
                Locale.getDefault()
            ).format(getFilterTimeUseCase(DURATION_UPPER_BOUND))
        )
    }
}
