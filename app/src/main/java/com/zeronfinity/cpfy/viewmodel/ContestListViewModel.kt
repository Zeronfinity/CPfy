package com.zeronfinity.cpfy.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.END_TIME
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.START_TIME
import com.zeronfinity.core.usecase.*
import com.zeronfinity.cpfy.framework.network.pojo.ClistContestObjectResponse
import com.zeronfinity.cpfy.viewmodel.helpers.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ContestListViewModel @ViewModelInject constructor(
    private val fetchAndPersistServerContestsUseCase: FetchAndPersistServerContestsUseCase,
    private val fetchAndPersistServerPlatformsUseCase: FetchAndPersistServerPlatformsUseCase,
    private val getFilterDurationUseCase: GetFilterDurationUseCase,
    private val getFilterTimeUseCase: GetFilterTimeUseCase,
    private val getFilteredContestListUseCase: GetFilteredContestListUseCase,
    getFilteredContestListFlowUseCase: GetFilteredContestListFlowUseCase,
    private val getOrderedPlatformListUseCase: GetOrderedPlatformListUseCase,
    private val getPlatformListUseCase: GetPlatformListUseCase,
    private val isFilterLowerBoundTodayUseCase: IsFilterLowerBoundTodayUseCase,
    private val getFilterDaysAfterTodayUseCase: GetFilterDaysAfterTodayUseCase,
) : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _clistWebViewLiveDataEv = MutableLiveData<Event<Boolean>>()
    private val _errorToastIncomingLiveDataEv = MutableLiveData<Event<String>>()

    private val _contestListLiveData = MutableLiveData<List<Contest>>()

    val clistWebViewLiveDataEv: LiveData<Event<Boolean>>
        get() = _clistWebViewLiveDataEv
    val errorToastIncomingLiveDataEv: LiveData<Event<String>>
        get() = _errorToastIncomingLiveDataEv

    val contestListLiveData: LiveData<List<Contest>>
        get() = _contestListLiveData

    val contestListLiveDataFlow = getFilteredContestListFlowUseCase().asLiveData()

    val platformListLiveData: LiveData<List<Platform>> = liveData {
        getPlatformListUseCase().collect {
            emit(getOrderedPlatformListUseCase(it))
        }
    }

    fun fetchContestList() {
        logD("fetchContestList() started")

        if (_contestListLiveData.value == null) {
            refreshContestList()
        }

        val simpleDateFormatUtc =
            SimpleDateFormat(ClistContestObjectResponse.apiDateFormat, Locale.US)
        simpleDateFormatUtc.timeZone = TimeZone.getTimeZone("UTC")

        coroutineScope.launch {
            var startTimeLowerBound = getFilterTimeUseCase(START_TIME_LOWER_BOUND)
            var startTimeUpperBound = getFilterTimeUseCase(START_TIME_UPPER_BOUND)
            var endTimeLowerBound = getFilterTimeUseCase(END_TIME_LOWER_BOUND)
            var endTimeUpperBound = getFilterTimeUseCase(END_TIME_UPPER_BOUND)

            if (isFilterLowerBoundTodayUseCase(START_TIME)) {
                val calendar = GregorianCalendar.getInstance()
                calendar.time = Date()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)

                startTimeLowerBound = calendar.time

                calendar.add(Calendar.DAY_OF_YEAR, getFilterDaysAfterTodayUseCase(START_TIME))
                startTimeUpperBound = calendar.time
            }

            if (isFilterLowerBoundTodayUseCase(END_TIME)) {
                val calendar = GregorianCalendar.getInstance()
                calendar.time = Date()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)

                endTimeLowerBound = calendar.time

                calendar.add(Calendar.DAY_OF_YEAR, getFilterDaysAfterTodayUseCase(END_TIME))
                endTimeUpperBound = calendar.time
            }

            val fetchResult = fetchAndPersistServerContestsUseCase(
                mapOf(
                    "start__gte" to simpleDateFormatUtc.format(startTimeLowerBound),
                    "start__lte" to simpleDateFormatUtc.format(startTimeUpperBound),
                    "end__gte" to simpleDateFormatUtc.format(endTimeLowerBound),
                    "end__lte" to simpleDateFormatUtc.format(endTimeUpperBound),
                    "duration__gte" to getFilterDurationUseCase(DURATION_LOWER_BOUND).toString(),
                    "duration__lte" to getFilterDurationUseCase(DURATION_UPPER_BOUND).toString(),
                    "order_by" to "start"
                )
            )

            logD("fetchResult: [$fetchResult]")

            when (fetchResult) {
                is FetchAndPersistServerContestsUseCase.Result.Success -> refreshContestList()
                is FetchAndPersistServerContestsUseCase.Result.Error -> _errorToastIncomingLiveDataEv.postValue(
                    Event(fetchResult.errorMsg)
                )
                is FetchAndPersistServerContestsUseCase.Result.UnauthorizedError -> _clistWebViewLiveDataEv.postValue(
                    Event(true)
                )
            }
        }
    }

    fun fetchPlatformList() {
        logD("fetchPlatformList() started")

        coroutineScope.launch {
            val fetchResult = fetchAndPersistServerPlatformsUseCase()

            logD("fetchResult: [$fetchResult]")

            when (fetchResult) {
                is FetchAndPersistServerPlatformsUseCase.Result.Success -> {
                    logD("fetchPlatformList() succeeded")
                }
                is FetchAndPersistServerPlatformsUseCase.Result.Error -> _errorToastIncomingLiveDataEv.postValue(
                    Event(fetchResult.errorMsg)
                )
                is FetchAndPersistServerPlatformsUseCase.Result.UnauthorizedError -> _clistWebViewLiveDataEv.postValue(
                    Event(true)
                )
            }
        }
    }

    fun refreshContestList() {
        coroutineScope.launch {
            _contestListLiveData.postValue(getFilteredContestListUseCase())
        }
    }
}
