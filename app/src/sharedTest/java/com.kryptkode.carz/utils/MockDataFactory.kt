package com.kryptkode.carz.utils

import com.kryptkode.carz.data.model.CarBuildDate
import com.kryptkode.carz.data.model.CarManufacturer
import com.kryptkode.carz.data.model.CarType
import com.kryptkode.carz.data.service.response.BuildDatesResponse
import com.kryptkode.carz.data.service.response.CarTypeResponse
import com.kryptkode.carz.data.service.response.ManufacturerResponse
import com.kryptkode.carz.utils.DataFactory.randomInt
import com.kryptkode.carz.utils.DataFactory.randomString

object MockDataFactory {

    const val DEFAULT_PAGE_SIZE = 15
    const val DEFAULT_PAGE = 0

    fun makeFakeBuildDateResponse(): BuildDatesResponse {
        val dates = mutableMapOf<String, String>()
        repeat(4) {
            dates["200$it"] = "200_$it"
        }
        return BuildDatesResponse(dates)
    }

    fun makeFakeCarTypeResponse(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        totalCount: Int = Int.MAX_VALUE
    ): CarTypeResponse {
        val carTypes = mutableMapOf<String, String>()
        repeat(4) {
            carTypes["t0$it"] = "type_$it"
        }
        return CarTypeResponse(
            page,
            pageSize,
            totalCount,
            carTypes
        )
    }

    fun makeFakeManufacturerResponse(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        totalCount: Int = Int.MAX_VALUE
    ): ManufacturerResponse {
        val manufacturers = mutableMapOf<String, String>()
        repeat(4) {
            manufacturers["0$it"] = "manufacturer_$it"
        }
        return ManufacturerResponse(
            page,
            pageSize,
            totalCount,
            manufacturers
        )
    }

    fun makeFakeCarManufacturer(): CarManufacturer {
        return CarManufacturer(
            randomString(),
            "name_".plus(randomInt())
        )
    }

    fun makeFakeCarType(): CarType {
        return CarType(
            randomString(),
            "type_".plus(randomInt()),
        )
    }

    fun makeFakeCarBuildDate(): CarBuildDate {
        return CarBuildDate(
            randomString(),
            "date_".plus(randomInt()),
        )
    }
}
