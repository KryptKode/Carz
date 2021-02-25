package com.kryptkode.carz.utils

import com.kryptkode.carz.data.service.CarApiService
import com.kryptkode.carz.data.service.interceptor.ApiKeyInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(ApiKeyInterceptor())
    .build()

private val moshi: Moshi
    get() = Moshi.Builder().build()

fun makeTestCarApiService(client: OkHttpClient, mockWebServer: MockWebServer): CarApiService = Retrofit.Builder()
    .baseUrl(mockWebServer.url("/"))
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
    .create(CarApiService::class.java)

fun MockWebServer.mockHttpResponse(body: String, responseCode: Int) = enqueue(
    MockResponse()
        .setResponseCode(responseCode)
        .setBody(body)
)
