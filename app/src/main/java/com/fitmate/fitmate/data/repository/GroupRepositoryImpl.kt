package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.source.remote.GroupService
import com.fitmate.fitmate.domain.repository.GroupRepository

class GroupRepositoryImpl(private val groupService: GroupService) : GroupRepository {
    override suspend fun getFilteredFitGroups(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int): GroupResponse {
        // return categoryService.getGroup(withMaxGroup, category, pageNumber, pageSize)
        return groupService.getGroup()
    }

    override suspend fun getFitGroupDetail(fitGroupId: Int): GroupDetailResponse {
        // return groupService.getGroupDetail(fitGroupId)
        return groupService.getGroupDetail()
    }

    override suspend fun fetchFitVotes(): Result<List<MyFitGroupVote>> {
        return Result.success(groupService.getMyFitGroupVotes().body()!!.fitGroupList)
    }
}