package com.kryptkode.carz.data.service

import com.kryptkode.carz.data.service.response.BuildDatesResponse
import com.kryptkode.carz.data.service.response.CarTypeResponse
import com.kryptkode.carz.data.service.response.ManufacturerResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CarApiService {

    @GET("v1/car-types/manufacturer")
    suspend fun getManufacturers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): ManufacturerResponse

    @GET("v1/car-types/main-types")
    suspend fun getCarTypes(
        @Query("manufacturer") manufacturer: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): CarTypeResponse

    @GET("v1/car-types/built-dates")
    suspend fun getBuildDates(
        @Query("manufacturer") manufacturer: String,
        @Query("main-type") mainType: String,
    ): BuildDatesResponse
}
