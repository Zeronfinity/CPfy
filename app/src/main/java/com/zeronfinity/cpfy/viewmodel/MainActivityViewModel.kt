package com.zeronfinity.cpfy.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.entity.ServerContestInfoResponse.ResponseStatus.SUCCESS
import com.zeronfinity.cpfy.model.UseCases
import com.zeronfinity.cpfy.model.network.pojo.ClistContestObjectResponse.Companion.simpleDateFormatUtc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainActivityViewModel @ViewModelInject constructor(
    private val useCases: UseCases
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
            val serverContestInfoResponse = useCases.fetchServerContestInfoUseCase(
                mapOf(
                    "end__gt" to simpleDateFormatUtc.format(Date()),
                    "end__lt" to simpleDateFormatUtc.format(Date(calendar.timeInMillis)),
                    "order_by" to "end"
                )
            )

            if (serverContestInfoResponse.responseStatus == SUCCESS) {
                useCases.removeAllContestsUseCase()
                serverContestInfoResponse.contestList?.let { useCases.addContestListUseCase(it) }
                serverContestInfoResponse.platformList?.let { useCases.addPlatformListUseCase(it) }
                contestListLiveData.postValue(true)
            } else {
                if (serverContestInfoResponse.errorCode != null) {
                    errorToastIncomingLiveData.postValue("Error ${serverContestInfoResponse.errorCode}: ${serverContestInfoResponse.errorDesc}")
                } else if (serverContestInfoResponse.errorDesc != null) {
                    errorToastIncomingLiveData.postValue(serverContestInfoResponse.errorDesc);
                } else {
                    errorToastIncomingLiveData.postValue("Unknown Network Error!")
                }
            }
        }
    }
}
