package com.kryptkode.carz.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarManufacturer(
    val id: String,
    val name: String,
) : Parcelable
