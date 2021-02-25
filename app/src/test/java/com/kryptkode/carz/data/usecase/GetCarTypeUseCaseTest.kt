package com.kryptkode.carz.data.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kryptkode.cardinfofinder.utils.MainCoroutineRule
import com.kryptkode.cardinfofinder.utils.runBlockingTest
import com.kryptkode.carz.data.dispatcher.TestDispatcher
import com.kryptkode.carz.data.error.FakeErrorHandler
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.data.model.PagingDataState
import com.kryptkode.carz.data.service.FakeCarApiService
import com.kryptkode.carz.utils.MockDataFactory.makeFakeCarTypeResponse
import java.io.IOException
import java.util.Locale
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCarTypeUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var apiService: FakeCarApiService
    private lateinit var errorHandler: FakeErrorHandler
    private lateinit var dispatchers: TestDispatcher

    private lateinit var sut: GetCarTypeUseCase

    @Before
    fun setup() {
        apiService = FakeCarApiService()
        errorHandler = FakeErrorHandler()
        dispatchers = TestDispatcher(coroutineRule.testDispatcher)
        sut = GetCarTypeUseCase(apiService, errorHandler, dispatchers)
    }

    @Test
    fun `executes emits loading state initially`() = coroutineRule.runBlockingTest {
        sut.execute("", "", 0).test {
            assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute on empty query and with page greater than totalPageCount emits EndOfPage`() =
        coroutineRule.runBlockingTest {
            sut.totalPageCount = 0

            sut.execute("", "", 1).test {
                assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
                assertThat(expectItem()).isEqualTo(PagingDataState.EndOfPage)
                expectComplete()
            }
        }

    @Test
    fun `execute on empty query and with page less than totalPageCount emits data`() =
        coroutineRule.runBlockingTest {
            val response = makeFakeCarTypeResponse()
            apiService.carTypeResponse = response

            val data = response.carTypes.map {
                CarType(it.key, it.value)
            }

            sut.execute("", "", 0).test {
                assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
                assertThat(expectItem()).isEqualTo(PagingDataState.Success(data))
                expectComplete()
            }
        }

    @Test
    fun `fetchCarTypes passes params to apiService`() = coroutineRule.runBlockingTest {
        val page = 2
        val manufacturer = "020"
        val response = makeFakeCarTypeResponse(page = page)
        apiService.carTypeResponse = response

        sut.fetchCarTypes(manufacturer, page)

        assertThat(apiService.page).isEqualTo(page)
        assertThat(apiService.manufacturer).isEqualTo(manufacturer)
    }

    @Test
    fun `fetchCarTypes sets totalPageCount`() = coroutineRule.runBlockingTest {
        val pageCount = 20
        val response = makeFakeCarTypeResponse(totalCount = pageCount)
        apiService.carTypeResponse = response

        sut.fetchCarTypes("2020", 2)

        assertThat(sut.totalPageCount).isEqualTo(pageCount)
    }

    @Test
    fun `fetchCarTypes add to linkedMap`() = coroutineRule.runBlockingTest {
        val response = makeFakeCarTypeResponse()
        apiService.carTypeResponse = response

        sut.fetchCarTypes("2020", 2)

        val data = response.carTypes.map {
            it.key to CarType(it.key, it.value)
        }.toMap()

        assertThat(sut.carTypes).isEqualTo(data)
    }

    @Test
    fun `execute with query emits search results`() = coroutineRule.runBlockingTest {
        val response = makeFakeCarTypeResponse(page = 0, totalCount = 2)
        apiService.carTypeResponse = response

        val query = "type_1"

        val data = response.carTypes.map {
            CarType(it.key, it.value)
        }.filter {
            it.name.toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US))
        }

        sut.execute("", query, 0).test {
            assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
            assertThat(expectItem()).isEqualTo(PagingDataState.Success(data))
            assertThat(expectItem()).isEqualTo(PagingDataState.Success(data))
            assertThat(expectItem()).isEqualTo(PagingDataState.Success(data))
            expectComplete()
        }
    }

    @Test
    fun `execute emits error state on exception`() = coroutineRule.runBlockingTest {
        val message = "Error occurred"
        val exception = IOException(message)

        apiService.exception = exception

        sut.execute("", "", 0).test {
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

        sut.execute("", "", 0).test {
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

        sut.execute("", "", 0).test {
            assertThat(expectItem()).isEqualTo(PagingDataState.Loading)
            assertThat(expectItem()).isEqualTo(PagingDataState.Error(message))
            expectComplete()
        }
    }
}
