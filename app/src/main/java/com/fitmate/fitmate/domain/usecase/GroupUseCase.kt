package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.domain.repository.GroupRepository
import javax.inject.Inject

class GroupUseCase @Inject constructor(private val groupRepository: GroupRepository) {
    suspend operator fun invoke(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int): GroupResponse {
        return groupRepository.getFilteredFitGroups(withMaxGroup, category, pageNumber, pageSize)
    }

    suspend operator fun invoke(fitGroupId: Int): GroupDetailResponse =groupRepository.getFitGroupDetail(fitGroupId)

    suspend fun fetchFitVotes(): Result<List<MyFitGroupVote>> = groupRepository.fetchFitVotes()
}