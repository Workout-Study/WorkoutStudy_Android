package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.RequestRegisterFitGroupBodyDto
import com.fitmate.fitmate.data.model.dto.RegisterFitGroupResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterFitGroupService {

    @POST("fit-group-service/groups")
    suspend fun registerFitGroup(@Body requestBody: RequestRegisterFitGroupBodyDto): Response<RegisterFitGroupResponseDto>
}