package com.kryptkode.carz.ui.cartypes

import com.kryptkode.carz.data.model.CarType

data class CarTypesViewState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = true,
    val errorMessage: String = "",
    val types: List<CarType> = emptyList(),
)
