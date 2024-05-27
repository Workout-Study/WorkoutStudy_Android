package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.data.model.dto.VoteResponse
import com.fitmate.fitmate.data.source.remote.VoteService
import com.fitmate.fitmate.domain.repository.VoteRepository
import retrofit2.Response

class VoteRepositoryImpl(private val voteService: VoteService): VoteRepository {

    override suspend fun myFitGroupVotes(requestUserId: Int): Result<List<MyFitGroupVote>> {
        return Result.success(voteService.getMyFitGroupVotes(requestUserId).body()!!.fitGroupList)
    }

    override suspend fun getEachGroupVotes(fitGroupId: Int, userId: Int): Response<EachFitResponse> {
        return voteService.getEachFitGroupVotes(fitGroupId, userId)
    }

    override suspend fun getFitMateProgress(fitGroupId: Int): Response<FitGroupProgress> {
        return voteService.getFitMateProgress(fitGroupId)
    }

    override suspend fun registerVote(voteRequest: VoteRequest): Response<VoteResponse> {
        return voteService.registerVote(voteRequest)
    }
}