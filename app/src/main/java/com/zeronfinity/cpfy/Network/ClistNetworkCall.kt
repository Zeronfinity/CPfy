package com.zeronfinity.cpfy.Network

import android.content.Context
import android.widget.Toast
import com.zeronfinity.cpfy.BuildConfig
import com.zeronfinity.cpfy.Model.ClistServerResponse
import com.zeronfinity.cpfy.Model.ResultWrapper
import com.zeronfinity.cpfy.R
import kotlinx.coroutines.Dispatchers

suspend fun getContestData(applicationContext: Context, params: Map<String, String>) : ClistServerResponse? {
    val apiInterface = RetrofitClistApiClient.getClient().create(RetrofitClistApiInterface::class.java)
    val wrappedResult = safeNetworkCall(Dispatchers.Default) {
        apiInterface.getContestData(applicationContext.getString(R.string.clist_api_username),
                                    BuildConfig.CLIST_API_KEY, params
                                )}

    var clistServerResponse: ClistServerResponse? = null

    when (wrappedResult) {
        is ResultWrapper.Success -> {
            if (wrappedResult.value.code() != 200) {
                Toast.makeText(applicationContext,
                    "Error ${wrappedResult.value.code()}: ${wrappedResult.value.message()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            clistServerResponse = wrappedResult.value.body()
        }
        is ResultWrapper.GenericError -> {
            if (wrappedResult.code != null) {
                Toast.makeText(applicationContext,
                    "Error ${wrappedResult.code}: ${wrappedResult.error?.error_description}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(applicationContext,
                    "Unknown Network Error!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        is ResultWrapper.NetworkError -> {
            Toast.makeText(applicationContext,"Network Failure: IOException!", Toast.LENGTH_SHORT).show()
        }
    }

    return clistServerResponse
}
