package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CertificationTargetGroupService {


    //@GET("group")
    //@GET("807f76c3-68e2-440e-8db3-37005d3a06b1")
    @GET("fit-group-service/groups/filters/{user-id}")
    suspend fun getTargetGroupList(@Path("user-id") userId:Int): Response<CertificationTargetFitGroupResponseDto>
}