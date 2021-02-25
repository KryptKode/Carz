package com.kryptkode.carz.data.service.interceptor

import com.kryptkode.carz.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrlWithParameterKey = originalUrl.newBuilder()
            .addQueryParameter(KEY, BuildConfig.API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrlWithParameterKey)

        return chain.proceed(newRequest.build())
    }

    companion object {
        private const val KEY = "wa_key"
    }
}
