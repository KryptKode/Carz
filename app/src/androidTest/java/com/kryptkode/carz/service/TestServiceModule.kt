package com.kryptkode.carz.service

import com.kryptkode.carz.data.service.CarApiService
import com.kryptkode.carz.data.service.ServiceModule
import com.kryptkode.carz.utils.makeTestCarApiService
import com.kryptkode.carz.utils.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ServiceModule::class]
)
interface TestServiceModule {
    companion object {

        @Provides
        fun mockWebServer(): MockWebServer {
            return MockWebServer()
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return okHttpClient
        }

        @Provides
        @Singleton
        fun provideCarApiServiceService(okHttpClient: OkHttpClient, mockWebServer: MockWebServer): CarApiService {
            return makeTestCarApiService(okHttpClient, mockWebServer)
        }
    }
}
