package com.kryptkode.carz.ui.manufacturer

import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.data.model.PagingDataState
import com.kryptkode.carz.data.usecase.GetManufacturersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.parcelize.Parcelize

@HiltViewModel
class ManufacturerViewModel @Inject constructor(
    private val getManufacturers: GetManufacturersUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutableViewState = MutableStateFlow(ManufacturerViewState())
    val viewState = mutableViewState.asStateFlow()

    @VisibleForTesting
    val stateReducer = { oldState: ManufacturerViewState, dataState: PagingDataState<List<CarManufacturer>> ->
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
                manufacturers = dataState.data,
            )
            is PagingDataState.EndOfPage -> oldState.copy(
                loading = false,
                error = false,
            )
        }
    }

    init {

        if (!savedStateHandle.contains(PARAM_KEY)) {
            refreshAllData()
        }

        savedStateHandle.getLiveData<Params>(PARAM_KEY)
            .asFlow()
            .flatMapLatest {
                getManufacturers.execute(it.page, it.refresh)
            }
            .scan(ManufacturerViewState()) { previous, result ->
                stateReducer(previous, result)
            }.onEach {
                mutableViewState.value = it
            }
            .launchIn(viewModelScope)
    }

    fun getManufacturers(page: Int) {
        if (shouldGetManufacturers(page)) {
            savedStateHandle.set(PARAM_KEY, Params(page))
        }
    }

    private fun shouldGetManufacturers(page: Int): Boolean {
        return savedStateHandle.get<Params>(PARAM_KEY)?.page != page
    }

    fun refreshData() {
        savedStateHandle.set(PARAM_KEY, savedStateHandle.get<Params>(PARAM_KEY))
    }

    fun refreshAllData() {
        savedStateHandle.set(PARAM_KEY, Params(refresh = true))
    }

    @Parcelize
    data class Params(
        val page: Int = DEFAULT_PAGE,
        val refresh: Boolean = false
    ) : Parcelable

    companion object {
        private const val DEFAULT_PAGE = 0
        @VisibleForTesting
        const val PARAM_KEY = "param_key"
    }
}
