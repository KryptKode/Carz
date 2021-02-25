package com.kryptkode.carz.data.usecase

import androidx.annotation.VisibleForTesting
import com.kryptkode.carz.data.dispatcher.AppDispatchers
import com.kryptkode.carz.data.error.ErrorHandler
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.data.model.PagingDataState
import com.kryptkode.carz.data.service.CarApiService
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCarTypeUseCase @Inject constructor(
    private val service: CarApiService,
    private val errorHandler: ErrorHandler,
    private val dispatchers: AppDispatchers,
) {

    @VisibleForTesting
    val carTypes = linkedMapOf<String, CarType>()

    @VisibleForTesting
    var totalPageCount = Int.MAX_VALUE

    @VisibleForTesting
    var currentPage = Int.MIN_VALUE

    fun execute(manufacturer: String, query: String, page: Int): Flow<PagingDataState<List<CarType>>> = flow {
        try {
            emit(PagingDataState.Loading)

            if (query.isEmpty()) {
                if (page > totalPageCount) {
                    emit(PagingDataState.EndOfPage)
                } else {
                    fetchCarTypes(manufacturer, page)
                    emit(PagingDataState.Success(carTypes.values.toList()))
                }
            } else {

                if (totalPageCount == Int.MAX_VALUE) {
                    fetchCarTypes(manufacturer, page)
                    emitSearchResults(query)
                }

                emitSearchResults(query)

                for (i in currentPage + 1 until totalPageCount) {
                    fetchCarTypes(manufacturer, i)
                    emitSearchResults(query)
                }
            }
        } catch (e: Exception) {
            emit(PagingDataState.Error(errorHandler.getErrorMessage(e)))
        }
    }.flowOn(dispatchers.io)

    private suspend fun FlowCollector<PagingDataState<List<CarType>>>.emitSearchResults(
        query: String
    ) {
        emit(
            PagingDataState.Success(
                carTypes.values.filter {
                    it.name.toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US))
                }.toList()
            )
        )
    }

    @VisibleForTesting
    suspend fun fetchCarTypes(manufacturer: String, page: Int) {
        val response = service.getCarTypes(manufacturer, page, PAGE_SIZE)
        totalPageCount = response.totalPageCount
        currentPage = page
        response.carTypes.forEach { item ->
            carTypes[item.key] = CarType(item.key, item.value)
        }
    }

    companion object {
        private const val PAGE_SIZE = 15
    }
}
