package com.zeronfinity.cpfy.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.repository.FilterTimeRangeRepository
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.usecase.FetchServerContestInfoUseCase
import com.zeronfinity.core.usecase.GetFilterDurationUseCase
import com.zeronfinity.core.usecase.GetFilterTimeUseCase
import com.zeronfinity.cpfy.model.network.pojo.ClistContestObjectResponse.Companion.simpleDateFormatUtc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class ContestListViewModel @ViewModelInject constructor(
    private val getFilterDurationUseCase: GetFilterDurationUseCase,
    private val getFilterTimeUseCase: GetFilterTimeUseCase,
    private val fetchServerContestInfoUseCase: FetchServerContestInfoUseCase
) : ViewModel() {
    private val LOG_TAG = ContestListViewModel::class.simpleName

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val numberOfDaysBeforeContestsEnd = 7

    val contestListUpdatedLiveData = MutableLiveData<Boolean>()
    val errorToastIncomingLiveData = MutableLiveData<String>()

    fun fetchContestListAndPersist() {
        Log.d(LOG_TAG, "fetchContestListAndPersist started")

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        coroutineScope.launch {
            val fetchResult = fetchServerContestInfoUseCase(
                mapOf(
                    "start__gte" to simpleDateFormatUtc.format(getFilterTimeUseCase(START_TIME_LOWER_BOUND)),
                    "start__lte" to simpleDateFormatUtc.format(getFilterTimeUseCase(START_TIME_UPPER_BOUND)),
                    "end__gte" to simpleDateFormatUtc.format(getFilterTimeUseCase(END_TIME_LOWER_BOUND)),
                    "end__lte" to simpleDateFormatUtc.format(getFilterTimeUseCase(END_TIME_UPPER_BOUND)),
                    "duration__gte" to getFilterDurationUseCase(DURATION_LOWER_BOUND).toString(),
                    "duration__lte" to getFilterDurationUseCase(DURATION_UPPER_BOUND).toString(),
                    "order_by" to "start"
                )
            )

            when (fetchResult) {
                is FetchServerContestInfoUseCase.Result.Success -> contestListUpdatedLiveData.postValue(fetchResult.value)
                is FetchServerContestInfoUseCase.Result.Error -> errorToastIncomingLiveData.postValue(fetchResult.errorMsg)
            }
        }
    }

    fun updateContestList() {
        contestListUpdatedLiveData.postValue(true)
    }
}
