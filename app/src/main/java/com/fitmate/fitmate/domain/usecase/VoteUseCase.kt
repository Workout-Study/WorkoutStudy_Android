package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.EachVoteCertificationResponseDto
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteRequestDto
import com.fitmate.fitmate.domain.repository.VoteRepository
import retrofit2.Response
import javax.inject.Inject

class VoteUseCase @Inject constructor(private val voteRepository: VoteRepository) {

    suspend fun myFitGroupVotes(requestUserId: Int): Result<List<MyFitGroupVote>> = voteRepository.myFitGroupVotes(requestUserId)

    suspend fun eachFitGroupVotes(fitGroupId: Int, userId: Int, withOwn: Int): Response<EachVoteCertificationResponseDto> = voteRepository.getEachGroupVotes(fitGroupId, userId, withOwn)

    suspend fun getFitMateProgress(fitGroupId: Int): Response<FitGroupProgress> = voteRepository.getFitMateProgress(fitGroupId)

    suspend fun registerVote(voteRequestDto: VoteRequestDto) = voteRepository.registerVote(voteRequestDto)

    suspend fun updateVote(voteRequestDto: VoteRequestDto) = voteRepository.updateVote(voteRequestDto)

}