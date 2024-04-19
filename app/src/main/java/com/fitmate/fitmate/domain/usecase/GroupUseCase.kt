package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.domain.repository.GroupRepository
import retrofit2.Response
import javax.inject.Inject

class GroupUseCase @Inject constructor(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int): GroupResponse {
        return groupRepository.getFilteredFitGroups(withMaxGroup, category, pageNumber, pageSize)
    }

    suspend operator fun invoke(fitGroupId: Int): GroupDetailResponse = groupRepository.getFitGroupDetail(fitGroupId)

    suspend fun myFitGroupVotes(): Result<List<MyFitGroupVote>> = groupRepository.myFitGroupVotes()

    suspend fun eachFitGroupVotes(): Response<EachFitResponse> = groupRepository.getEachGroupVotes()

    suspend fun getFitMateList(): Response<FitGroupDetail> = groupRepository.getFitMateList()

    suspend fun getFitMateProgress(): Response<FitGroupProgress> = groupRepository.getFitMateProgress()

    suspend fun registerVote(voteRequest: VoteRequest) = groupRepository.registerVote(voteRequest)
}