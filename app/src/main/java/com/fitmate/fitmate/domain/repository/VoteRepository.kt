package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.data.model.dto.VoteResponse
import retrofit2.Response

interface VoteRepository {

    suspend fun getEachGroupVotes(fitGroupId: Int, userId: Int): Response<EachFitResponse>

    suspend fun myFitGroupVotes(requestUserId: Int): Result<List<MyFitGroupVote>>

    suspend fun getFitMateProgress(fitGroupId: Int): Response<FitGroupProgress>

    suspend fun registerVote(voteRequest: VoteRequest): Response<VoteResponse>
}