package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.FitGroupFilter
import com.fitmate.fitmate.data.model.dto.FitMateKickRequestUserIdDto
import com.fitmate.fitmate.data.model.dto.FitMateRequest
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.RegisterResponse
import com.fitmate.fitmate.data.model.dto.ResponseFitMateKickDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {
    @GET("/fit-group-service/groups/filters")
    suspend fun fitGroupFilter(
        @Query("withMaxGroup") withMaxGroup: Boolean,
        @Query("category") category: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
    ): FitGroupFilter

    @GET("/fit-group-service/groups/filters")
    suspend fun fitGroupAll(
        @Query("withMaxGroup") withMaxGroup: Boolean,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): FitGroupFilter

    @GET("/fit-group-service/groups/{fit-group-id}")
    suspend fun getFitGroupDetail(
        @Path("fit-group-id") fitGroupId: Int
    ): GetFitGroupDetail


    @GET("/fit-group-service/mates/{fit-group-id}")
    suspend fun getFitMateList(
        @Path("fit-group-id") fitGroupId: Int
    ): Response<GetFitMateList>

    @POST("/fit-group-service/mates")
    suspend fun registerFitMate(
        @Body fitMateRequest: FitMateRequest
    ): RegisterResponse

    @HTTP(method = "DELETE", path = "/fit-group-service/managements/mates/{fit-group-id}/{user-id}",hasBody = true)
    suspend fun kickFitMate(
        @Path("fit-group-id") fitGroupId: Int,
        @Path("user-id") userId: Int,
        @Body requestUserId: FitMateKickRequestUserIdDto
    ): Response<ResponseFitMateKickDto>
}