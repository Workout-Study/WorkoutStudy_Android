package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse
import com.fitmate.fitmate.data.model.dto.MyFitResponse
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.data.model.dto.VoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GroupService {

//    @GET("/fit-group-service/groups/filters")
//    suspend fun getGroup(
//        @Query("withMaxGroup") withMaxGroup: Boolean,
//        @Query("category") category: Int,
//        @Query("pageNumber") pageNumber: Int,
//        @Query("pageSize") pageSize: Int,
//    ): GroupResponse

    @GET("2fc7f451-202d-406e-ac11-329dea59e691")
    suspend fun getGroup(): GroupResponse

//    @GET("/fit-group-service/groups/")
//    suspend fun getGroupDetail(
//        @Query("fit-group-id") fitGroupId: Int
//    ): GroupDetailResponse

    @GET("8ec6a3d5-9b6b-45b5-ad6d-29cd0bc58a73")
    suspend fun getGroupDetail(): GroupDetailResponse

//    @GET("/my-fit-service/my-fits/certifications/need-votes)
//    suspend fun getMyFitGroupVotes(
//        @Query("requestUserId") requestUserId: Int,
//    ): Response<MyFitResponse>
    @GET("c8d5a2b2-743e-429f-b984-c302e7deffb2")
    suspend fun getMyFitGroupVotes(): Response<MyFitResponse>

    @GET("19e89fb6-dad0-4cdc-acc3-7a26603f2cad")
    suspend fun getEachFitGroupVotes(): Response<EachFitResponse>

    @GET("1a68ebeb-9c5d-42aa-97dc-45ed1071d34f")
    suspend fun getFitMateList(): Response<FitGroupDetail>

    @GET("df0f9622-f2db-4fd0-bedb-eb940e6c7c9c")
    suspend fun getFitMateProgress(): Response<FitGroupProgress>

    @POST("3d8abaa3-42c8-4bd7-a23d-7e69585c168c")
    suspend fun registerVote(@Body voteRequest: VoteRequest): Response<VoteResponse>
}