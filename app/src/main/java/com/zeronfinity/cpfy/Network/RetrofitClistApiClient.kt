package com.zeronfinity.cpfy.Network

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

    fun getClient() = retrofit
}
