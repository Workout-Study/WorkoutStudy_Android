package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CertificationTargetGroupService {

    @GET("fit-group-service/groups/filters/{user-id}")
    suspend fun getTargetGroupList(@Path("user-id") userId:Int): Response<CertificationTargetFitGroupResponseDto>
}