package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.MyFitResponse
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.data.model.dto.VoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VoteService {

    @GET("/my-fit-service/my-fits/certifications/progresses/{fit-group-id}")
    suspend fun getFitMateProgress( // Fit Certification Progress List By fit group id
        @Path("fit-group-id") fitGroupId: Int
    ): Response<FitGroupProgress>

    @POST("/my-fit-service/votes")
    suspend fun registerVote(   // Register Vote
        @Body voteRequest: VoteRequest
    ): Response<VoteResponse>
    @GET("/my-fit-service/my-fits/certifications/need-votes")
    suspend fun getMyFitGroupVotes(   // Need Vote Fit Certification List
        @Query("requestUserId") requestUserId: Int
    ): Response<MyFitResponse>

    @GET("/my-fit-service/certifications/filters/{fit-group-id}/{user-id}")
    suspend fun getEachFitGroupVotes(   // Proceeding Fit Certification List By fit group id
        @Path("fit-group-id") fitGroupId: Int,
        @Path("user-id") userId: Int
    ): Response<EachFitResponse>

}