package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.EachVoteCertificationResponseDto
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.MyFitResponse
import com.fitmate.fitmate.data.model.dto.VoteRequestDto
import com.fitmate.fitmate.data.model.dto.VoteResponseDto
import com.fitmate.fitmate.data.model.dto.VoteUpdateResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface VoteService {

    @GET("/my-fit-service/my-fits/certifications/progresses/{fit-group-id}")
    suspend fun getFitMateProgress( // Fit Certification Progress List By fit group id
        @Path("fit-group-id") fitGroupId: Int
    ): Response<FitGroupProgress>

    @POST("/my-fit-service/votes")
    suspend fun registerVote(   // Register Vote
        @Body voteRequestDto: VoteRequestDto
    ): Response<VoteResponseDto>

    @PUT("/my-fit-service/votes")
    suspend fun updateVote(   // Update Vote
        @Body voteUpdateRequestDto: VoteRequestDto
    ): Response<VoteUpdateResponseDto>

    @GET("/my-fit-service/my-fits/certifications/need-votes")
    suspend fun getMyFitGroupVotes(   // Need Vote Fit Certification List
        @Query("requestUserId") requestUserId: Int
    ): Response<MyFitResponse>

    @GET("/my-fit-service/certifications/filters/{fit-group-id}/{user-id}")
    suspend fun getEachFitGroupVotes(   // Proceeding Fit Certification List By fit group id
        @Path("fit-group-id") fitGroupId: Int,
        @Path("user-id") userId: Int,
        @Query("withOwn") withOwn: Int
    ): Response<EachVoteCertificationResponseDto>

}