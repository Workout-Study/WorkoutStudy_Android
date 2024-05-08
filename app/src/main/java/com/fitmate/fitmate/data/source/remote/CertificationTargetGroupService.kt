package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CertificationTargetGroupService {

    /*@GET("fit-group-service/groups/filters")*/
    //@GET("group")
    @GET("cfeffd3f-44fb-4795-aaa1-9d9ff04d33c9")
    suspend fun getTargetGroupList(@Query("user-id") userId:String): Response<CertificationTargetFitGroupResponseDto>
}