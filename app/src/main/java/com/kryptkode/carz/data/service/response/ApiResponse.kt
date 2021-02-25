package com.kryptkode.carz.data.service.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ManufacturerResponse(
    @get:Json(name = "page") val page: Int,
    @get:Json(name = "pageSize") val pageSize: Int,
    @get:Json(name = "totalPageCount") val totalPageCount: Int,
    @get:Json(name = "wkda") val manufacturers: Map<String, String>,
)

@JsonClass(generateAdapter = true)
data class CarTypeResponse(
    @get:Json(name = "page") val page: Int,
    @get:Json(name = "pageSize") val pageSize: Int,
    @get:Json(name = "totalPageCount") val totalPageCount: Int,
    @get:Json(name = "wkda") val carTypes: Map<String, String>,
)

@JsonClass(generateAdapter = true)
data class BuildDatesResponse(
    @get:Json(name = "wkda") val buildDates: Map<String, String>,
)
