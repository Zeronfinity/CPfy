package com.zeronfinity.cpfy.framework.network.clist

import android.app.Application
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.usecase.GetCookieUseCase
import com.zeronfinity.cpfy.R
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClistApiClient(
    application: Application,
    getCookieUseCase: GetCookieUseCase
) {
    private val apiBaseUrl = application.getString(R.string.clist_base_url)
    private var retrofit: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder().addNetworkInterceptor(interceptor)
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val rawCookieString =
                        getCookieUseCase(application.getString(R.string.clist_session_cookie))
                    logD("Raw cookie string: [$rawCookieString]")
                    val cookieList = ArrayList<Cookie>()
                    rawCookieString?.let {
                        Cookie.parse(
                            url,
                            it
                        )?.let { it1 -> cookieList.add(it1) }
                    }
                    return cookieList
                }
            })
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    fun getClistApi(): RetrofitClistApiInterface =
        retrofit.create(RetrofitClistApiInterface::class.java)
}
