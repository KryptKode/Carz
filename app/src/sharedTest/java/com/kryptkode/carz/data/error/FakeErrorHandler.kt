package com.kryptkode.carz.data.error

class FakeErrorHandler : ErrorHandler {
    var message: String = ""
    var exception: Throwable? = null
    override fun getErrorMessage(e: Throwable?): String {
        exception = e
        return message
    }
}
