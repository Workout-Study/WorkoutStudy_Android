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

    @GET("178873d4-c41a-484a-a247-1331e26f7421")
    suspend fun getGroup(): GroupResponse

//    @GET("/fit-group-service/groups/")
//    suspend fun getGroupDetail(
//        @Query("fit-group-id") fitGroupId: Int
//    ): GroupDetailResponse

    @GET("1ee11220-486a-4000-84ac-d5a8a0665034")
    suspend fun getGroupDetail(): GroupDetailResponse

//    @GET("/my-fit-service/my-fits/certifications/need-votes)
//    suspend fun getMyFitGroupVotes(
//        @Query("requestUserId") requestUserId: Int,
//    ): Response<MyFitResponse>
    @GET("878f9c64-f400-4652-9d9e-b4718356e1f6")
    suspend fun getMyFitGroupVotes(): Response<MyFitResponse>

    @GET("98b35a9e-e3f9-4b4d-8d6a-c4402296a699")
    suspend fun getEachFitGroupVotes(): Response<EachFitResponse>

    @GET("db3e8aeb-8bf9-4f86-8aff-cfd72d1003c4")
    suspend fun getFitMateList(): Response<FitGroupDetail>

    @GET("4f5e4667-96f3-4c19-9b1a-4a70ba6636fb")
    suspend fun getFitMateProgress(): Response<FitGroupProgress>

    @POST("0eaf8c2d-c61f-4ac9-9a64-67f2f5700646")
    suspend fun registerVote(@Body voteRequest: VoteRequest): Response<VoteResponse>
}