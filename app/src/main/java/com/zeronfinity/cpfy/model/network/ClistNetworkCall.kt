package com.zeronfinity.cpfy.model.network

import android.app.Application
import com.zeronfinity.cpfy.BuildConfig
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.framework.network.ResultWrapper
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiClient
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiInterface
import com.zeronfinity.cpfy.framework.network.safeNetworkCall
import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

suspend fun getContestData(application: Application, params: Map<String, String>) : ResultWrapper<Response<ClistServerResponse>> {
    val apiInterface = RetrofitClistApiClient.getClient()
        .create(RetrofitClistApiInterface::class.java)

    return safeNetworkCall(Dispatchers.Default) {
        apiInterface.getContestData(
            application.getString(R.string.clist_api_username),
            BuildConfig.CLIST_API_KEY,
            params
        )
    }
}
