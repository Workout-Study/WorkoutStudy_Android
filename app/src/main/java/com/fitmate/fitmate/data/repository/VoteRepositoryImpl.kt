package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.dto.EachVoteCertificationResponseDto
import com.fitmate.fitmate.data.model.dto.FitMateProgressDto
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteRequestDto
import com.fitmate.fitmate.data.model.dto.VoteResponseDto
import com.fitmate.fitmate.data.model.dto.VoteUpdateResponseDto
import com.fitmate.fitmate.data.source.remote.VoteService
import com.fitmate.fitmate.domain.repository.VoteRepository
import retrofit2.Response

class VoteRepositoryImpl(private val voteService: VoteService): VoteRepository {

    override suspend fun myFitGroupVotes(requestUserId: Int): Result<List<MyFitGroupVote>> {
        return Result.success(voteService.getMyFitGroupVotes(requestUserId).body()!!.fitGroupList)
    }

    override suspend fun getEachGroupVotes(fitGroupId: Int, userId: Int, withOwn: Int): Response<EachVoteCertificationResponseDto> {
        return voteService.getEachFitGroupVotes(fitGroupId, userId, withOwn)
    }

    override suspend fun getFitMateProgress(fitGroupId: Int): Response<FitMateProgressDto> {
        return voteService.getFitMateProgress(fitGroupId)
    }

    override suspend fun registerVote(voteRequestDto: VoteRequestDto): Response<VoteResponseDto> {
        return voteService.registerVote(voteRequestDto)
    }

    override suspend fun updateVote(voteUpdateRequestDto: VoteRequestDto): Response<VoteUpdateResponseDto> {
        return voteService.updateVote(voteUpdateRequestDto)
    }
}