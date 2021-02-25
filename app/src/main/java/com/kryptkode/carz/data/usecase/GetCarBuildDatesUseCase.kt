package com.kryptkode.carz.data.usecase

import com.kryptkode.carz.data.dispatcher.AppDispatchers
import com.kryptkode.carz.data.error.ErrorHandler
import com.kryptkode.carz.data.model.CarBuildDate
import com.kryptkode.carz.data.model.DataState
import com.kryptkode.carz.data.service.CarApiService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCarBuildDatesUseCase @Inject constructor(
    private val service: CarApiService,
    private val errorHandler: ErrorHandler,
    private val dispatchers: AppDispatchers,
) {

    fun execute(manufacturer: String, type: String): Flow<DataState<List<CarBuildDate>>> = flow {
        emit(DataState.Loading)
        try {
            val result = service.getBuildDates(manufacturer, type)
            emit(
                DataState.Success(
                    result.buildDates.map { item ->
                        CarBuildDate(item.key, item.value)
                    }
                )
            )
        } catch (e: Exception) {
            emit(DataState.Error(errorHandler.getErrorMessage(e)))
        }
    }.flowOn(dispatchers.io)
}
