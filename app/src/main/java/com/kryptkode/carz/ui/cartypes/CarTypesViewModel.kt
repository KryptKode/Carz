package com.kryptkode.carz.ui.cartypes

import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.data.model.PagingDataState
import com.kryptkode.carz.data.usecase.GetCarTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@HiltViewModel
class CarTypesViewModel @Inject constructor(
    private val getCarTypes: GetCarTypeUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutableViewState = MutableStateFlow(CarTypesViewState())
    val viewState = mutableViewState.asStateFlow()

    @VisibleForTesting
    val stateReducer = { oldState: CarTypesViewState, dataState: PagingDataState<List<CarType>> ->
        when (dataState) {
            is PagingDataState.Error -> oldState.copy(
                loading = false,
                error = true,
                errorMessage = dataState.message,
            )
            is PagingDataState.Loading -> oldState.copy(loading = true, error = false)
            is PagingDataState.Success -> oldState.copy(
                loading = false,
                error = false,
                empty = dataState.data.isEmpty(),
                types = dataState.data,
            )
            is PagingDataState.EndOfPage -> oldState.copy(
                loading = false,
                error = false,
            )
        }
    }

    init {
        savedStateHandle.getLiveData<CarTypeParams>(PARAM_KEY)
            .asFlow()
            .flatMapLatest {
                Timber.d("Parameters: $it")
                getCarTypes.execute(it.manufacturer, it.searchQuery, it.page)
            }
            .scan(CarTypesViewState()) { previous, result ->
                stateReducer(previous, result)
            }.onEach {
                mutableViewState.value = it
            }
            .launchIn(viewModelScope)
    }

    fun getCarTypes(page: Int = DEFAULT_PAGE) {
        val currentParam = savedStateHandle.get<CarTypeParams>(PARAM_KEY)
        if (currentParam?.page != page) {
            savedStateHandle.set(
                PARAM_KEY,
                currentParam?.copy(page = page)
            )
        }
    }

    fun refreshData() {
        savedStateHandle.set(PARAM_KEY, savedStateHandle.get<CarTypeParams>(PARAM_KEY))
    }

    fun searchCarTypes(manufacturer: String, query: String = "") {
        if (!savedStateHandle.contains(PARAM_KEY)) {
            savedStateHandle.set(PARAM_KEY, CarTypeParams(manufacturer))
        }

        if (savedStateHandle.get<CarTypeParams>(PARAM_KEY)?.searchQuery != query) {
            val param = savedStateHandle.get<CarTypeParams>(PARAM_KEY)?.copy(
                searchQuery = query,
                page = DEFAULT_PAGE
            )
            savedStateHandle.set(
                PARAM_KEY,
                param
            )
        }
    }

    @Parcelize
    data class CarTypeParams(
        val manufacturer: String,
        val page: Int = DEFAULT_PAGE,
        val searchQuery: String = ""
    ) : Parcelable

    companion object {
        private const val DEFAULT_PAGE = 0

        @VisibleForTesting
        const val PARAM_KEY = "page_key"
    }
}
