package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.FitOffListDto
import com.fitmate.fitmate.data.model.dto.FitOffRequestDto
import com.fitmate.fitmate.data.model.dto.FitOffResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FitOffService {
    @POST("/my-fit-service/fit-offs")
    suspend fun registerFitOff(
        @Body fitOffRequest: FitOffRequestDto
    ):Response<FitOffResponseDto>

    @GET("/my-fit-service/fit-offs/users/{user-id}")
    suspend fun getFitOffByUserId(
        @Path("user-id") userId: Int
    ): Response<FitOffListDto>


    @GET("/my-fit-service/fit-offs/{fit-group-id}")
    suspend fun getFitOffByGroupId(
        @Path("fit-group-id") groupId: Int
    ): Response<FitOffListDto>
}