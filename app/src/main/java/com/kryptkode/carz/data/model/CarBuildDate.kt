package com.kryptkode.carz.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarBuildDate(
    val id: String,
    val year: String,
) : Parcelable
