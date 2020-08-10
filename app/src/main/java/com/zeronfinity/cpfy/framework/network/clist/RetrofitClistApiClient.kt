package com.zeronfinity.cpfy.framework.network.clist

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClistApiClient {
    private const val apiBaseUrl = "https://clist.by"
    private var retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    fun getClistApi(): RetrofitClistApiInterface =
        retrofit.create(RetrofitClistApiInterface::class.java)
}
