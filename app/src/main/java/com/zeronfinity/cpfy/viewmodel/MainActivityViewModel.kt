package com.zeronfinity.cpfy.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.usecase.FetchServerContestInfoUseCase
import com.zeronfinity.core.usecase.FetchServerContestInfoUseCase.Result.Error
import com.zeronfinity.core.usecase.FetchServerContestInfoUseCase.Result.Success
import com.zeronfinity.cpfy.model.network.pojo.ClistContestObjectResponse.Companion.simpleDateFormatUtc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainActivityViewModel @ViewModelInject constructor(
    private val fetchServerContestInfoUseCase: FetchServerContestInfoUseCase
) : ViewModel() {
    private val LOG_TAG = MainActivityViewModel::class.simpleName

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val numberOfDaysBeforeContestsEnd = 7

    val contestListLiveData = MutableLiveData<Boolean>()
    val errorToastIncomingLiveData = MutableLiveData<String>()

    fun fetchContestListAndPersist() {
        Log.d(LOG_TAG, "fetchContestListAndPersist started")

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        coroutineScope.launch {
            val fetchResult = fetchServerContestInfoUseCase(
                mapOf(
                    "end__gt" to simpleDateFormatUtc.format(Date()),
                    "end__lt" to simpleDateFormatUtc.format(Date(calendar.timeInMillis)),
                    "order_by" to "end"
                )
            )

            when (fetchResult) {
                is Success -> contestListLiveData.postValue(fetchResult.value)
                is Error -> errorToastIncomingLiveData.postValue(fetchResult.errorMsg)
            }
        }
    }
}
