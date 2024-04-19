package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.FitRecordHistoryResponse
import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyFitService {

    /*@GET("my-fit-service/my-fits/certifications/progresses")*/
    @GET("3a134b83-fa24-4699-bd8c-29a57bf8ae2c")
    suspend fun getMyFitProgress(@Query("requestUserId") userId: String): Response<MyFitProgressResponseDto>

    /*@GET("my-fit-service/records/filters")*/
    @GET("dbdd3b6c-daa5-4bf7-aacc-f73b7838b0d0")
    suspend fun getMyFitRecordHistory(
        @Query("userId") userId: String,
        @Query("recordEndStartDate") startDate: String,
        @Query("recordEndEndDate") endDate: String,
    ):Response<FitRecordHistoryResponse>
}