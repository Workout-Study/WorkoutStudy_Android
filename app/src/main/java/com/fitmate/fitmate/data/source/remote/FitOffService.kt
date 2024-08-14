package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.FitOffRequestDto
import com.fitmate.fitmate.data.model.dto.FitOffResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FitOffService {
    @POST("/my-fit-service/fit-offs")
    suspend fun registerFitOff(
        @Body fitOffRequest: FitOffRequestDto
    ):Response<FitOffResponseDto>
}