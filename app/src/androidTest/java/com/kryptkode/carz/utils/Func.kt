package com.kryptkode.carz.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import java.io.IOException
import java.io.InputStreamReader

fun getString(@StringRes resId: Int): String {
    return ApplicationProvider.getApplicationContext<Context>()
        .getString(resId)
}

fun readStringFromFile(fileName: String): String {
    try {
        val inputStream = ApplicationProvider.getApplicationContext<Context>().assets.open(fileName)
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    } catch (e: IOException) {
        throw e
    }
}
