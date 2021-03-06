package com.kryptkode.carz.data.service

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule {

    companion object {
        @Provides
        @Singleton
        fun provideCarApiServiceService(): CarApiService {
            return ServiceFactory.createCarService()
        }
    }
}
