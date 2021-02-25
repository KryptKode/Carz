package com.kryptkode.carz.data.service

import com.kryptkode.carz.data.service.response.BuildDatesResponse
import com.kryptkode.carz.data.service.response.CarTypeResponse
import com.kryptkode.carz.data.service.response.ManufacturerResponse
import com.kryptkode.carz.utils.MockDataFactory

class FakeCarApiService : CarApiService {

    var manufacturerResponse: ManufacturerResponse? = null
    var carTypeResponse: CarTypeResponse? = null
    var exception: Exception? = null
    var buildDateResponse: BuildDatesResponse? = null
    var manufacturer: String = ""
    var mainType: String = ""
    var page: Int = MockDataFactory.DEFAULT_PAGE
    var pageSize: Int = MockDataFactory.DEFAULT_PAGE_SIZE

    override suspend fun getBuildDates(manufacturer: String, mainType: String): BuildDatesResponse {
        exception?.let { throw it }
        this.manufacturer = manufacturer
        this.mainType = mainType
        return buildDateResponse!!
    }

    override suspend fun getCarTypes(manufacturer: String, page: Int, pageSize: Int): CarTypeResponse {
        exception?.let { throw it }
        this.manufacturer = manufacturer
        this.page = page
        this.pageSize = pageSize
        return carTypeResponse!!
    }

    override suspend fun getManufacturers(page: Int, pageSize: Int): ManufacturerResponse {
        exception?.let { throw it }
        this.page = page
        this.pageSize = pageSize
        return manufacturerResponse!!
    }
}
