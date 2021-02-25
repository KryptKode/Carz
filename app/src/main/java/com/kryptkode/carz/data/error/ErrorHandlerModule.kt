package com.kryptkode.carz.data.error

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ErrorHandlerModule {
    @Binds
    @Singleton
    fun bindAppDispatchers(errorHandlerImpl: ErrorHandlerImpl): ErrorHandler
}
