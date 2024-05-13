package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.FitRecordHistoryResponse
import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyFitService {

    /*@GET("my-fit-service/my-fits/certifications/progresses")*/
    //@GET("a53a2763-eac4-4d66-8d0c-387849b4208f") <-데이터가 있는 화면 보고싶을 때 사용
    @GET("fe1a3df9-827e-4618-985b-60814f617229") //<-데이터가 없는 화면 보고 싶을 떄 사용
    suspend fun getMyFitProgress(@Query("requestUserId") userId: String): Response<MyFitProgressResponseDto>

    /*@GET("my-fit-service/records/filters")*/
    @GET("4dec0c6c-1be6-4725-8808-658e4c23ef2b")
    suspend fun getMyFitRecordHistory(
        @Query("userId") userId: String,
        @Query("recordEndStartDate") startDate: String,
        @Query("recordEndEndDate") endDate: String,
    ):Response<FitRecordHistoryResponse>
}