package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CertificationTargetGroupService {

    /*@GET("fit-group-service/groups/filters")*/
    @GET("group")
    suspend fun getTargetGroupList(@Query("user-id") userId:String): Response<CertificationTargetFitGroupResponseDto>
}