package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyFitService {

    @GET("my-fit-service/my-fits/certifications/progresses")
    suspend fun getMyFitProgress(@Query("requestUserId") userId: String): Response<MyFitProgressResponseDto>
}