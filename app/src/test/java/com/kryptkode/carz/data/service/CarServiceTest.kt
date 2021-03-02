package com.kryptkode.carz.data.service

import com.google.common.truth.Truth.assertThat
import com.kryptkode.carz.BuildConfig
import com.kryptkode.carz.util.getResourceAsString
import com.kryptkode.carz.utils.makeTestCarApiService
import com.kryptkode.carz.utils.mockHttpResponse
import com.kryptkode.carz.utils.okHttpClient
import java.net.HttpURLConnection
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.HttpException

class CarServiceTest {
    private val mockWebServer = MockWebServer()
    private val sut = makeTestCarApiService(okHttpClient, mockWebServer)

    @Test
    fun `getManufacturers has API_KEY`() = runBlocking {

        mockWebServer.mockHttpResponse("manufacturer_success.json".resource(), HttpURLConnection.HTTP_OK)

        sut.getManufacturers(PAGE, PAGE_SIZE)

        assertThat(
            mockWebServer.takeRequest()
                .requestUrl?.queryParameter("wa_key")
        ).isEqualTo(BuildConfig.API_KEY)
    }

    @Test
    fun `getManufacturers on successful response returns data`() = runBlocking {

        mockWebServer.mockHttpResponse("manufacturer_success.json".resource(), HttpURLConnection.HTTP_OK)

        val result = sut.getManufacturers(PAGE, PAGE_SIZE)

        assertThat(result.manufacturers.size).isEqualTo(79)
        assertThat(result.manufacturers["020"]).isEqualTo("Abarth")
        assertThat(result.manufacturers["195"]).isEqualTo("Daewoo")
    }

    @Test
    fun `getManufacturers on empty response returns empty data`() = runBlocking {

        mockWebServer.mockHttpResponse("manufacturer_empty.json".resource(), HttpURLConnection.HTTP_OK)

        val result = sut.getManufacturers(PAGE, PAGE_SIZE)

        assertThat(result.totalPageCount).isEqualTo(0)
        assertThat(result.manufacturers).isEmpty()
    }

    @Test
    @Throws(HttpException::class)
    fun `getManufacturers fails with network errors`() = runBlocking {

        mockWebServer.mockHttpResponse("manufacturer_success.json".resource(), HttpURLConnection.HTTP_GATEWAY_TIMEOUT)

        val exception = try {
            sut.getManufacturers(PAGE, PAGE_SIZE)
            null
        } catch (e: Throwable) {
            e
        }
        assertThat(exception).isInstanceOf(HttpException::class.java)
        assertThat(exception?.message).isEqualTo("HTTP 504 Server Error")
    }

    @Test
    fun `getCarTypes on successful response returns data`() = runBlocking {

        mockWebServer.mockHttpResponse("types_success.json".resource(), HttpURLConnection.HTTP_OK)

        val result = sut.getCarTypes("020", PAGE, PAGE_SIZE)

        assertThat(result.carTypes.size).isEqualTo(11)
        assertThat(result.carTypes["500"]).isEqualTo("500")
        assertThat(result.carTypes["Punto"]).isEqualTo("Punto")
    }

    @Test
    fun `getCarTypes on empty response returns empty data`() = runBlocking {

        mockWebServer.mockHttpResponse("types_empty.json".resource(), HttpURLConnection.HTTP_OK)

        val result = sut.getCarTypes("020", PAGE, PAGE_SIZE)

        assertThat(result.totalPageCount).isEqualTo(0)
        assertThat(result.carTypes).isEmpty()
    }

    @Test
    @Throws(HttpException::class)
    fun `getCarTypes fails with network errors`() = runBlocking {

        mockWebServer.mockHttpResponse("types_success.json".resource(), HttpURLConnection.HTTP_GATEWAY_TIMEOUT)

        val exception = try {
            sut.getCarTypes("020", PAGE, PAGE_SIZE)
            null
        } catch (e: Throwable) {
            e
        }
        assertThat(exception).isInstanceOf(HttpException::class.java)
        assertThat(exception?.message).isEqualTo("HTTP 504 Server Error")
    }

    @Test
    fun `getBuildDates on successful response returns data`() = runBlocking {

        mockWebServer.mockHttpResponse("build_dates_success.json".resource(), HttpURLConnection.HTTP_OK)

        val result = sut.getBuildDates("020", "Punto")

        assertThat(result.buildDates.size).isEqualTo(12)
        assertThat(result.buildDates["2008"]).isEqualTo("2008")
        assertThat(result.buildDates["2013"]).isEqualTo("2013")
    }

    @Test
    fun `getBuildDates on empty response returns empty data`() = runBlocking {

        mockWebServer.mockHttpResponse("build_dates_empty.json".resource(), HttpURLConnection.HTTP_OK)

        val result = sut.getBuildDates("020", "Punto")

        assertThat(result.buildDates).isEmpty()
    }

    @Test
    @Throws(HttpException::class)
    fun `getBuildDates fails with network errors`() = runBlocking {

        mockWebServer.mockHttpResponse("build_dates_success.json".resource(), HttpURLConnection.HTTP_GATEWAY_TIMEOUT)

        val exception = try {
            sut.getBuildDates("020", "Punto")
            null
        } catch (e: Throwable) {
            e
        }
        assertThat(exception).isInstanceOf(HttpException::class.java)
        assertThat(exception?.message).isEqualTo("HTTP 504 Server Error")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun String.resource(): String {
        return getResourceAsString(this)
    }

    companion object {
        private const val PAGE = 0
        private const val PAGE_SIZE = 15
    }
}
