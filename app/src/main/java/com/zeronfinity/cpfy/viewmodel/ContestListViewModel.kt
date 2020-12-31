package com.zeronfinity.cpfy.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.usecase.FetchServerContestInfoUseCase
import com.zeronfinity.core.usecase.FetchServerPlatformInfoUseCase
import com.zeronfinity.core.usecase.GetFilterDurationUseCase
import com.zeronfinity.core.usecase.GetFilterTimeUseCase
import com.zeronfinity.cpfy.model.network.pojo.ClistContestObjectResponse
import com.zeronfinity.cpfy.viewmodel.helpers.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ContestListViewModel @ViewModelInject constructor(
    private val getFilterDurationUseCase: GetFilterDurationUseCase,
    private val getFilterTimeUseCase: GetFilterTimeUseCase,
    private val fetchServerContestInfoUseCase: FetchServerContestInfoUseCase,
    private val fetchServerPlatformInfoUseCase: FetchServerPlatformInfoUseCase
) : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val numberOfDaysBeforeContestsEnd = 7

    private val _contestListUpdatedLiveDataEv = MutableLiveData<Event<Boolean>>()
    private val _platformListUpdatedLiveDataEv = MutableLiveData<Event<Boolean>>()
    private val _clistWebViewLiveDataEv = MutableLiveData<Event<Boolean>>()
    private val _errorToastIncomingLiveDataEv = MutableLiveData<Event<String>>()

    val contestListUpdatedLiveDataEv: LiveData<Event<Boolean>>
            get() = _contestListUpdatedLiveDataEv
    val platformListUpdatedLiveDataEv: LiveData<Event<Boolean>>
        get() = _platformListUpdatedLiveDataEv
    val clistWebViewLiveDataEv: LiveData<Event<Boolean>>
            get() = _clistWebViewLiveDataEv
    val errorToastIncomingLiveDataEv: LiveData<Event<String>>
            get() = _errorToastIncomingLiveDataEv

    fun fetchContestList() {
        logD("fetchContestListAndPersist() started")

        updateContestList()

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        val simpleDateFormatUtc = SimpleDateFormat(ClistContestObjectResponse.apiDateFormat, Locale.US)
        simpleDateFormatUtc.timeZone = TimeZone.getTimeZone("UTC")

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

            logD("fetchResult: [$fetchResult]")

            when (fetchResult) {
                is FetchServerContestInfoUseCase.Result.Success -> updateContestList()
                is FetchServerContestInfoUseCase.Result.Error -> _errorToastIncomingLiveDataEv.postValue(Event(fetchResult.errorMsg))
                is FetchServerContestInfoUseCase.Result.UnauthorizedError -> _clistWebViewLiveDataEv.postValue(Event(true))
            }
        }
    }

    fun fetchPlatformList() {
        logD("fetchPlatformListAndPersist() started")

        coroutineScope.launch {
            val fetchResult = fetchServerPlatformInfoUseCase()

            logD("fetchResult: [$fetchResult]")

            when (fetchResult) {
                is FetchServerPlatformInfoUseCase.Result.Success -> {
                    if (fetchResult.isUpdateRequired) {
                        updatePlatformList()
                    }
                }
                is FetchServerPlatformInfoUseCase.Result.Error -> _errorToastIncomingLiveDataEv.postValue(Event(fetchResult.errorMsg))
                is FetchServerPlatformInfoUseCase.Result.UnauthorizedError -> _clistWebViewLiveDataEv.postValue(Event(true))
            }
        }
    }

    private fun updateContestList() {
        _contestListUpdatedLiveDataEv.postValue(Event(true))
    }

    private fun updatePlatformList() {
        _platformListUpdatedLiveDataEv.postValue(Event(true))
    }
}
