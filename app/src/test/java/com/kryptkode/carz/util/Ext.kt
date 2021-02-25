package com.kryptkode.carz.util

import com.google.common.io.Resources
import java.io.File
import java.net.URL

fun getResourceAsString(path: String): String {
    val uri: URL = Resources.getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
}
