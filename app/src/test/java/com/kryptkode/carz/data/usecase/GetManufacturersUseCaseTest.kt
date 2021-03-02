package com.kryptkode.carz.data.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kryptkode.carz.utils.MainCoroutineRule
import com.kryptkode.carz.utils.runBlockingTest
import com.kryptkode.carz.data.dispatcher.TestDispatcher
import com.kryptkode.carz.data.error.FakeErrorHandler
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.data.model.PagingDataState
import com.kryptkode.carz.data.service.FakeCarApiService
import com.kryptkode.carz.utils.MockDataFactory.makeFakeManufacturerResponse
import java.io.IOException
import org.junit.Rule
import org.junit.Test

class GetManufacturersUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val apiService = FakeCarApiService()
    private val errorHandler = FakeErrorHandler()
    private val dispatchers =  TestDispatcher(coroutineRule.testDispatcher)

    private val sut = GetManufacturersUseCase(apiService, errorHandler, dispatchers)

    @Test
    fun `executes emits loading state initially`() = coroutineRule.runBlockingTest {

        sut.execute(1).test {
            assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `executes with page greater than totalPageCount emits EndOfPage`() = coroutineRule.runBlockingTest {
        sut.totalPageCount = 0

        sut.execute(1).test {
            assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
            assertThat(expectItem()).isEqualTo(PagingDataState.EndOfPage)
            expectComplete()
        }
    }

    @Test
    fun `execute emits success state`() = coroutineRule.runBlockingTest {
        val response = makeFakeManufacturerResponse()
        apiService.manufacturerResponse = response

        sut.execute(0).test {
            assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
            assertThat(expectItem()).isInstanceOf(PagingDataState.Success::class.java)
            expectComplete()
        }
    }

    @Test
    fun `execute on success state sets totalPageCount`() = coroutineRule.runBlockingTest {
        val totalCount = 15
        val response = makeFakeManufacturerResponse(totalCount = totalCount)
        apiService.manufacturerResponse = response

        sut.execute(0).test {
            assertThat(sut.totalPageCount).isEqualTo(totalCount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute on success state adds to linked map`() = coroutineRule.runBlockingTest {
        val totalCount = 15
        val response = makeFakeManufacturerResponse(totalCount = totalCount)
        apiService.manufacturerResponse = response

        sut.execute(0).test {
            response.manufacturers.forEach {
                assertThat(sut.manufacturers[it.key]).isEqualTo(CarManufacturer(it.key, it.value))
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute on success state emits data`() = coroutineRule.runBlockingTest {
        val response = makeFakeManufacturerResponse()
        apiService.manufacturerResponse = response

        val data = response.manufacturers.map {
            CarManufacturer(it.key, it.value)
        }

        sut.execute(0).test {
            assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
            assertThat(expectItem()).isEqualTo(PagingDataState.Success(data))
            expectComplete()
        }
    }

    @Test
    fun `execute passes page to service`() = coroutineRule.runBlockingTest {
        val page = 2
        val response = makeFakeManufacturerResponse(page = page)
        apiService.manufacturerResponse = response

        sut.execute(page).test {
            assertThat(apiService.page).isEqualTo(page)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute emits error state on exception`() = coroutineRule.runBlockingTest {
        val message = "Error occurred"
        val exception = IOException(message)

        apiService.exception = exception

        sut.execute(0).test {
            assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
            assertThat(expectItem()).isInstanceOf(PagingDataState.Error::class.java)
            expectComplete()
        }
    }

    @Test
    fun `execute on exception calls error handler`() = coroutineRule.runBlockingTest {
        val exception = IOException("Error occurred")
        apiService.exception = exception

        val message = "Check internet"
        errorHandler.message = message

        sut.execute(0).test {
            assertThat(errorHandler.exception).isEqualTo(exception)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute emits on exception error state with ErrorHandler message`() = coroutineRule.runBlockingTest {
        val exception = IOException("Error occurred")
        apiService.exception = exception

        val message = "Check internet"
        errorHandler.message = message

        sut.execute(0).test {
            assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
            assertThat(expectItem()).isEqualTo(PagingDataState.Error(message))
            expectComplete()
        }
    }
}
