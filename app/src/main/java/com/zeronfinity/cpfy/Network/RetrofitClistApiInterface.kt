package com.zeronfinity.cpfy.Network

import com.zeronfinity.cpfy.Model.ClistServerResponse
import retrofit2.Response
import retrofit2.http.*


interface RetrofitClistApiInterface {

    @GET("/api/v1/json/contest/")
    suspend fun getContestData(@Query("username") userName: String, @Query("api_key") apiKey: String,
                       @QueryMap params: Map<String,String>): Response<ClistServerResponse>
}
