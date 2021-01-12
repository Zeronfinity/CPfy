package com.zeronfinity.cpfy.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_LOWER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterDurationEnum.DURATION_UPPER_BOUND
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeEnum.*
import com.zeronfinity.core.usecase.*
import com.zeronfinity.cpfy.framework.network.pojo.ClistContestObjectResponse
import com.zeronfinity.cpfy.viewmodel.helpers.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ContestListViewModel @ViewModelInject constructor(
    private val fetchAndPersistServerContestsUseCase: FetchAndPersistServerContestsUseCase,
    private val fetchAndPersistServerPlatformsUseCase: FetchAndPersistServerPlatformsUseCase,
    private val getFilterDurationUseCase: GetFilterDurationUseCase,
    private val getFilterTimeUseCase: GetFilterTimeUseCase,
    private val getFilteredContestListUseCase: GetFilteredContestListUseCase,
    getFilteredContestListFlowUseCase: GetFilteredContestListFlowUseCase,
    private val getOrderedPlatformListUseCase: GetOrderedPlatformListUseCase,
    private val getPlatformListUseCase: GetPlatformListUseCase
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
            val fetchResult = fetchAndPersistServerContestsUseCase(
                mapOf(
                    "start__gte" to simpleDateFormatUtc.format(
                        getFilterTimeUseCase(
                            START_TIME_LOWER_BOUND
                        )
                    ),
                    "start__lte" to simpleDateFormatUtc.format(
                        getFilterTimeUseCase(
                            START_TIME_UPPER_BOUND
                        )
                    ),
                    "end__gte" to simpleDateFormatUtc.format(
                        getFilterTimeUseCase(
                            END_TIME_LOWER_BOUND
                        )
                    ),
                    "end__lte" to simpleDateFormatUtc.format(
                        getFilterTimeUseCase(
                            END_TIME_UPPER_BOUND
                        )
                    ),
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
