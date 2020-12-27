package com.zeronfinity.cpfy.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.usecase.FetchServerContestInfoUseCase
import com.zeronfinity.cpfy.model.network.pojo.ClistContestObjectResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class ContestListViewModel @ViewModelInject constructor(
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
                    "end__gt" to ClistContestObjectResponse.simpleDateFormatUtc.format(Date()),
                    "end__lt" to ClistContestObjectResponse.simpleDateFormatUtc.format(Date(calendar.timeInMillis)),
                    "order_by" to "end"
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
