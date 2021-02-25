package com.kryptkode.carz.data.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kryptkode.cardinfofinder.utils.MainCoroutineRule
import com.kryptkode.cardinfofinder.utils.runBlockingTest
import com.kryptkode.carz.data.dispatcher.TestDispatcher
import com.kryptkode.carz.data.error.FakeErrorHandler
import com.kryptkode.carz.data.model.CarBuildDate
import com.kryptkode.carz.data.model.DataState
import com.kryptkode.carz.data.service.FakeCarApiService
import com.kryptkode.carz.utils.MockDataFactory.makeFakeBuildDateResponse
import java.io.IOException
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCarBuildDatesUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var apiService: FakeCarApiService
    private lateinit var errorHandler: FakeErrorHandler
    private lateinit var dispatchers: TestDispatcher

    private lateinit var sut: GetCarBuildDatesUseCase

    @Before
    fun setup() {
        apiService = FakeCarApiService()
        errorHandler = FakeErrorHandler()
        dispatchers = TestDispatcher(coroutineRule.testDispatcher)
        sut = GetCarBuildDatesUseCase(apiService, errorHandler, dispatchers)
    }

    @Test
    fun `executes emits loading state initially`() = coroutineRule.runBlockingTest {
        sut.execute("", "").test {
            assertThat(expectItem()).isEqualTo(DataState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute emits success state`() = coroutineRule.runBlockingTest {
        val manufacturer = "020"
        val type = "car_type_1"

        val response = makeFakeBuildDateResponse()
        apiService.buildDateResponse = response

        sut.execute(manufacturer, type).test {
            assertThat(expectItem()).isEqualTo(DataState.Loading)
            assertThat(expectItem()).isInstanceOf(DataState.Success::class.java)
            expectComplete()
        }
    }

    @Test
    fun `execute emits success state with correct data`() = coroutineRule.runBlockingTest {

        val response = makeFakeBuildDateResponse()
        apiService.buildDateResponse = response

        val data = response.buildDates.map {
            CarBuildDate(it.key, it.value)
        }

        sut.execute("020", "type").test {
            assertThat(expectItem()).isEqualTo(DataState.Loading)
            assertThat(expectItem()).isEqualTo(DataState.Success(data))
            expectComplete()
        }
    }

    @Test
    fun `execute on success calls apiService with passed param`() = coroutineRule.runBlockingTest {
        val manufacturer = "020"
        val type = "car_type_1"

        val response = makeFakeBuildDateResponse()
        apiService.buildDateResponse = response

        sut.execute(manufacturer, type).test {
            assertThat(apiService.manufacturer).isEqualTo(manufacturer)
            assertThat(apiService.mainType).isEqualTo(type)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `execute emits error state on exception`() = coroutineRule.runBlockingTest {
        val message = "Error occurred"
        val exception = IOException(message)

        apiService.exception = exception

        sut.execute("", "").test {
            assertThat(expectItem()).isEqualTo(DataState.Loading)
            assertThat(expectItem()).isInstanceOf(DataState.Error::class.java)
            expectComplete()
        }
    }

    @Test
    fun `execute on exception calls error handler`() = coroutineRule.runBlockingTest {
        val exception = IOException("Error occurred")
        apiService.exception = exception

        val message = "Check internet"
        errorHandler.message = message

        sut.execute("", "").test {
            assertThat(errorHandler.exception).isEqualTo(exception)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `execute emits on exception error state with ErrorHandler message`() = coroutineRule.runBlockingTest {
        val exception = IOException("Error occurred")
        apiService.exception = exception

        val message = "Check internet"
        errorHandler.message = message

        sut.execute("", "").test {
            assertThat(expectItem()).isEqualTo(DataState.Loading)
            assertThat(expectItem()).isEqualTo(DataState.Error(message))
            expectComplete()
        }
    }
}
