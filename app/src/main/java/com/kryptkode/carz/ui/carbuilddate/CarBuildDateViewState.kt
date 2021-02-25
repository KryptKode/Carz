package com.kryptkode.carz.ui.carbuilddate

import com.kryptkode.carz.data.model.CarBuildDate

data class CarBuildDateViewState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val errorMessage: String = "",
    val buildDates: List<CarBuildDate> = emptyList(),
)
