package com.kryptkode.carz.data.error

interface ErrorHandler {
    fun getErrorMessage(e: Throwable?): String
}
