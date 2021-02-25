package com.kryptkode.carz.ui.carbuilddate

import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.kryptkode.carz.data.model.CarBuildDate
import com.kryptkode.carz.data.model.DataState
import com.kryptkode.carz.data.usecase.GetCarBuildDatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.parcelize.Parcelize

@HiltViewModel
class CarBuildDateViewModel @Inject constructor(
    private val getCarBuildDates: GetCarBuildDatesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutableViewState = MutableStateFlow(CarBuildDateViewState())
    val viewState = mutableViewState.asStateFlow()

    @VisibleForTesting
    val stateReducer = { oldState: CarBuildDateViewState, dataState: DataState<List<CarBuildDate>> ->
        when (dataState) {
            is DataState.Error -> oldState.copy(
                loading = false,
                error = true,
                errorMessage = dataState.message,
            )
            is DataState.Loading -> oldState.copy(
                loading = true,
                error = false,
            )
            is DataState.Success -> oldState.copy(
                loading = false,
                error = false,
                buildDates = dataState.data,
            )
        }
    }

    init {
        savedStateHandle.getLiveData<CarBuildDateParams>(PARAM_KEY)
            .asFlow()
            .distinctUntilChanged()
            .flatMapLatest {
                getCarBuildDates.execute(it.manufacturer, it.carType)
            }
            .scan(CarBuildDateViewState()) { previous, result ->
                stateReducer(previous, result)
            }.onEach {
                mutableViewState.value = it
            }
            .launchIn(viewModelScope)
    }

    fun getCarBuildDates(manufacturerId: String, type: String) {
        if (shouldGetManufacturers(manufacturerId, type)) {
            savedStateHandle.set(PARAM_KEY, CarBuildDateParams(manufacturerId, type))
        }
    }

    private fun shouldGetManufacturers(manufacturerId: String, type: String): Boolean {
        return savedStateHandle.get<CarBuildDateParams>(PARAM_KEY) != CarBuildDateParams(manufacturerId, type)
    }

    fun refreshData() {
        savedStateHandle.set(PARAM_KEY, savedStateHandle.get<CarBuildDateParams>(PARAM_KEY))
    }

    @Parcelize
    data class CarBuildDateParams(
        val manufacturer: String,
        val carType: String,
    ) : Parcelable

    companion object {
        @VisibleForTesting
        const val PARAM_KEY = "page_key"
    }
}
