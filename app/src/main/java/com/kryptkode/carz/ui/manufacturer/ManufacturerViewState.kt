package com.kryptkode.carz.ui.manufacturer

import com.kryptkode.carz.data.model.CarManufacturer

data class ManufacturerViewState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = true,
    val errorMessage: String = "",
    val manufacturers: List<CarManufacturer> = emptyList(),
)
