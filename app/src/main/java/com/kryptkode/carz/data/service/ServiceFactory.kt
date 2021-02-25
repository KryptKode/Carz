package com.kryptkode.carz.data.service

import com.kryptkode.carz.BuildConfig
import com.kryptkode.carz.data.service.interceptor.ApiKeyInterceptor
import com.squareup.moshi.Moshi
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceFactory {

    private const val MAX_TIMEOUT_SECS = 60L

    fun createCarService(): CarApiService {
        return makeCarService(Moshi.Builder().build(), BuildConfig.DEBUG)
    }

    private fun makeCarService(moshi: Moshi, isDebug: Boolean): CarApiService {
        val okHttpClient = makeOkHttpClient(
            makeLoggingInterceptor((isDebug))
        )
        return makeCarService(okHttpClient, moshi)
    }

    private fun makeCarService(client: OkHttpClient, moshi: Moshi): CarApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(CarApiService::class.java)
    }

    private fun makeOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(interceptor)
            .connectTimeout(MAX_TIMEOUT_SECS, TimeUnit.SECONDS)
            .readTimeout(MAX_TIMEOUT_SECS, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}
