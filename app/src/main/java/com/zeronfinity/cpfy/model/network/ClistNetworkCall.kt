package com.zeronfinity.cpfy.model.network

import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.CustomApplication
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.framework.network.ErrorResponse
import com.zeronfinity.cpfy.framework.network.ResultWrapper
import com.zeronfinity.cpfy.framework.network.clist.RetrofitClistApiInterface
import com.zeronfinity.cpfy.framework.network.safeNetworkCall
import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.net.HttpURLConnection

class ClistNetworkCall(
    private val application: CustomApplication,
    private val apiInterface: RetrofitClistApiInterface
) {
    suspend fun getContestData(params: Map<String, String>): ResultWrapper<Response<ClistServerResponse>> {
        logD("params: [" + params.toString() + "]")

        val wrappedResult = safeNetworkCall(Dispatchers.Default) {
            apiInterface.getContestData(
                application.getString(R.string.clist_api_username),
                application.getClistApiKey(),
                params
            )
        }

        if (wrappedResult is ResultWrapper.Success) {
            if (wrappedResult.value.code() != HttpURLConnection.HTTP_OK) {
                return ResultWrapper.GenericError(wrappedResult.value.code(), ErrorResponse(wrappedResult.value.message(), emptyMap()))
            }
        }

        return wrappedResult
    }
}
