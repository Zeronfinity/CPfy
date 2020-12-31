package com.zeronfinity.cpfy.framework.network.clist

import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponseContests
import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponsePlatforms
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RetrofitClistApiInterface {

    @GET("/api/v1/json/contest/")
    suspend fun getContestData(@Query("username") userName: String, @Query("api_key") apiKey: String, @QueryMap params: Map<String,String>): Response<ClistServerResponseContests>

    @GET("/api/v1/json/resource/")
    suspend fun getPlatformData(@Query("username") userName: String, @Query("api_key") apiKey: String): Response<ClistServerResponsePlatforms>
}
