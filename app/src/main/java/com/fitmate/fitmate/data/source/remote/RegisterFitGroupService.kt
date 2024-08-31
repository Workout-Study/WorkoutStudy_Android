package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.RequestRegisterFitGroupBodyDto
import com.fitmate.fitmate.data.model.dto.RegisterFitGroupResponseDto
import com.fitmate.fitmate.data.model.dto.UpdateFitGroupResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RegisterFitGroupService {

    @POST("fit-group-service/groups")
    suspend fun registerFitGroup(@Body requestBody: RequestRegisterFitGroupBodyDto): Response<RegisterFitGroupResponseDto>

    @PUT("fit-group-service/groups/{fit-group-id}")
    suspend fun updateFitGroup(
        @Path("fit-group-id") fitGroupId: Int,
        @Body requestBody: RequestRegisterFitGroupBodyDto
    ): Response<UpdateFitGroupResponseDto>
}