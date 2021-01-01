package com.zeronfinity.cpfy.network

import com.zeronfinity.cpfy.framework.network.retrofit.RetrofitClistApiInterface
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ClistApiClientMock {
    private val mockWebServer = MockWebServer()
    private var retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    fun getClistApi(): RetrofitClistApiInterface =
        retrofit.create(RetrofitClistApiInterface::class.java)

    fun enqueMockServerResponse(response: MockResponse) {
        mockWebServer.enqueue(response)
    }

    fun getRequestMadeToMockServer() = mockWebServer.takeRequest()
}
