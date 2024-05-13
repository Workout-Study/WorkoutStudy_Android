package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.FitRecordHistoryResponse
import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyFitService {

    /*@GET("my-fit-service/my-fits/certifications/progresses")*/
    @GET("a53a2763-eac4-4d66-8d0c-387849b4208f")
    suspend fun getMyFitProgress(@Query("requestUserId") userId: String): Response<MyFitProgressResponseDto>

    /*@GET("my-fit-service/records/filters")*/
    @GET("359fb81c-8ccb-4576-8d6e-1bc091e7f3df")
    suspend fun getMyFitRecordHistory(
        @Query("userId") userId: String,
        @Query("recordEndStartDate") startDate: String,
        @Query("recordEndEndDate") endDate: String,
    ):Response<FitRecordHistoryResponse>
}