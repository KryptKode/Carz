package com.kryptkode.carz.data.usecase

import androidx.annotation.VisibleForTesting
import com.kryptkode.carz.data.dispatcher.AppDispatchers
import com.kryptkode.carz.data.error.ErrorHandler
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.data.model.PagingDataState
import com.kryptkode.carz.data.service.CarApiService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetManufacturersUseCase @Inject constructor(
    private val service: CarApiService,
    private val errorHandler: ErrorHandler,
    private val dispatchers: AppDispatchers,
) {

    @VisibleForTesting
    val manufacturers = linkedMapOf<String, CarManufacturer>()

    @VisibleForTesting
    var totalPageCount = Int.MAX_VALUE

    fun execute(page: Int, forceRefresh: Boolean = false): Flow<PagingDataState<List<CarManufacturer>>> = flow {

        try {
            emit(PagingDataState.Loading)

            if (forceRefresh) {
                manufacturers.clear()
            }

            if (page > totalPageCount) {
                emit(PagingDataState.EndOfPage)
            } else {
                val result = service.getManufacturers(page, PAGE_SIZE)
                totalPageCount = result.totalPageCount

                result.manufacturers.forEach { item ->
                    manufacturers[item.key] = CarManufacturer(item.key, item.value)
                }

                emit(PagingDataState.Success(manufacturers.values.toList()))
            }
        } catch (e: Exception) {
            emit(PagingDataState.Error(errorHandler.getErrorMessage(e)))
        }
    }.flowOn(dispatchers.io)

    companion object {
        private const val PAGE_SIZE = 15
    }
}
