package com.zeronfinity.cpfy.model.network

import android.app.Application
import com.zeronfinity.cpfy.BuildConfig
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.framework.SubApplication
import com.zeronfinity.cpfy.framework.network.ResultWrapper
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiInterface
import com.zeronfinity.cpfy.framework.network.safeNetworkCall
import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import javax.inject.Inject

class ClistNetworkCall(val application: Application) {
    @Inject
    lateinit var apiInterface: RetrofitClistApiInterface

    init {
        (application as SubApplication).getApplicationComponent().inject(this)
    }

    suspend fun getContestData(params: Map<String, String>): ResultWrapper<Response<ClistServerResponse>> {
        return safeNetworkCall(Dispatchers.Default) {
            apiInterface.getContestData(
                application.getString(R.string.clist_api_username),
                BuildConfig.CLIST_API_KEY,
                params
            )
        }
    }
}
