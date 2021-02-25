package com.kryptkode.carz.data.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatcher(
    private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
) : AppDispatchers {

    override val default: CoroutineDispatcher
        get() = dispatcher

    override val io: CoroutineDispatcher
        get() = dispatcher

    override val main: CoroutineDispatcher
        get() = dispatcher
}
