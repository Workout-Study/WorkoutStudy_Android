package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.domain.repository.VoteRepository
import retrofit2.Response
import javax.inject.Inject

class VoteUseCase @Inject constructor(private val voteRepository: VoteRepository) {

    suspend fun myFitGroupVotes(): Result<List<MyFitGroupVote>> = voteRepository.myFitGroupVotes()

    suspend fun eachFitGroupVotes(): Response<EachFitResponse> = voteRepository.getEachGroupVotes()

    suspend fun getFitMateProgress(): Response<FitGroupProgress> = voteRepository.getFitMateProgress()

    suspend fun registerVote(voteRequest: VoteRequest) = voteRepository.registerVote(voteRequest)

}