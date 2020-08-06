package com.zeronfinity.cpfy.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.repository.ContestRepository
import com.zeronfinity.core.repository.PlatformRepository
import com.zeronfinity.core.usecase.*
import com.zeronfinity.cpfy.model.ContestArrayList
import com.zeronfinity.cpfy.model.PlatformMap
import com.zeronfinity.cpfy.model.UseCases
import com.zeronfinity.cpfy.framework.network.ResultWrapper
import com.zeronfinity.cpfy.model.network.getContestData
import com.zeronfinity.cpfy.model.network.pojo.ClistContestObjectResponse.Companion.simpleDateFormatUtc
import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponse
import com.zeronfinity.cpfy.view.LOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainActivityViewModel(private val globalAppState: Application) : AndroidViewModel(globalAppState) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val numberOfDaysBeforeContestsEnd = 7

    private val contestRepository = ContestRepository(ContestArrayList())
    private val platformRepository = PlatformRepository(PlatformMap())

    private val useCases = UseCases(
        AddContestList(contestRepository),
        GetContest(contestRepository),
        GetContestCount(contestRepository),
        RemoveAllContests(contestRepository),
        AddPlatform(platformRepository),
        GetPlatformImageUrl(platformRepository),
        RemoveAllPlatforms(platformRepository)
    )

    val contestListLiveData = MutableLiveData<Boolean>()
    val errorToastIncomingLiveData = MutableLiveData<String>()

    fun fetchContestListFromClist() {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDaysBeforeContestsEnd)

        coroutineScope.launch {
            val wrappedResult =
                getContestData(
                    globalAppState,
                    mapOf(
                        "end__gt" to simpleDateFormatUtc.format(Date()),
                        "end__lt" to simpleDateFormatUtc.format(Date(calendar.timeInMillis)),
                        "order_by" to "end"
                    )
                )

            var clistServerResponse: ClistServerResponse? = null

            when (wrappedResult) {
                is ResultWrapper.Success -> {
                    if (wrappedResult.value.code() != 200) {
                        errorToastIncomingLiveData.postValue("Error ${wrappedResult.value.code()}: ${wrappedResult.value.message()}")
                    } else {
                        clistServerResponse = wrappedResult.value.body()
                    }
                }
                is ResultWrapper.GenericError -> {
                    if (wrappedResult.code != null) {
                        errorToastIncomingLiveData.postValue("Error ${wrappedResult.code}: ${wrappedResult.error?.error_description}")
                    } else {
                        errorToastIncomingLiveData.postValue("Unknown Network Error!")
                    }
                }
                is ResultWrapper.NetworkError -> {
                    errorToastIncomingLiveData.postValue("Network Failure: IOException!")
                }
            }

            Log.i(LOG_TAG, clistServerResponse.toString())

            if (clistServerResponse != null) {
                useCases.removeAllContests()
                useCases.addContestList(processFetchedData(clistServerResponse))
                contestListLiveData.postValue(true)
            }
        }
    }

    private fun processFetchedData(clistServerResponse: ClistServerResponse) =
            try {
                val list = clistServerResponse.contestList
                val contestList = ArrayList<Contest>()

                for (i in list.indices) {
                    contestList.add(list[i].toContest())
                    val key = list[i].platformResource.platformName
                    useCases.addPlatform(Platform(key, "https://clist.by" + list[i].platformResource.iconUrlSegment))
                }

                contestList
            } catch (e: Exception) {
                e.printStackTrace()
                ArrayList<Contest>()
            }
}
