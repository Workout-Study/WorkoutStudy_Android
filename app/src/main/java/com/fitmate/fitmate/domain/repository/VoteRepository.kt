package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.EachVoteCertificationResponseDto
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteRequestDto
import com.fitmate.fitmate.data.model.dto.VoteResponseDto
import com.fitmate.fitmate.data.model.dto.VoteUpdateResponseDto
import retrofit2.Response

interface VoteRepository {

    suspend fun getEachGroupVotes(fitGroupId: Int, userId: Int, withOwn: Int): Response<EachVoteCertificationResponseDto>

    suspend fun myFitGroupVotes(requestUserId: Int): Result<List<MyFitGroupVote>>

    suspend fun getFitMateProgress(fitGroupId: Int): Response<FitGroupProgress>

    suspend fun registerVote(voteRequestDto: VoteRequestDto): Response<VoteResponseDto>

    suspend fun updateVote(voteUpdateRequestDto: VoteRequestDto): Response<VoteUpdateResponseDto>
}