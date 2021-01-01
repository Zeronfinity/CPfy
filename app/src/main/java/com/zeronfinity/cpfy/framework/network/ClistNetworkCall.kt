package com.zeronfinity.cpfy.framework.network

import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.CustomApplication
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.framework.network.retrofit.RetrofitClistApiInterface
import com.zeronfinity.cpfy.framework.network.pojo.ClistServerResponseContests
import com.zeronfinity.cpfy.framework.network.pojo.ClistServerResponsePlatforms
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.net.HttpURLConnection

class ClistNetworkCall(
    private val application: CustomApplication,
    private val apiInterface: RetrofitClistApiInterface
) {
    suspend fun getContestData(params: Map<String, String>): ResultWrapper<Response<ClistServerResponseContests>> {
        logD("getContestData() started -> params: [$params]")

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

    suspend fun getPlatformData(): ResultWrapper<Response<ClistServerResponsePlatforms>> {
        logD("getPlatformData() started")

        val wrappedResult = safeNetworkCall(Dispatchers.Default) {
            apiInterface.getPlatformData(
                application.getString(R.string.clist_api_username),
                application.getClistApiKey()
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
