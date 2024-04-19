package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.data.model.dto.VoteResponse
import com.fitmate.fitmate.data.source.remote.GroupService
import com.fitmate.fitmate.domain.repository.GroupRepository
import retrofit2.Response

class GroupRepositoryImpl(private val groupService: GroupService) : GroupRepository {
    override suspend fun getFilteredFitGroups(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int): GroupResponse {
        // return categoryService.getGroup(withMaxGroup, category, pageNumber, pageSize)
        return groupService.getGroup()
    }

    override suspend fun getFitGroupDetail(fitGroupId: Int): GroupDetailResponse {
        // return groupService.getGroupDetail(fitGroupId)
        return groupService.getGroupDetail()
    }

    override suspend fun myFitGroupVotes(): Result<List<MyFitGroupVote>> {
        return Result.success(groupService.getMyFitGroupVotes().body()!!.fitGroupList)
    }

    override suspend fun getEachGroupVotes(): Response<EachFitResponse> {
        return groupService.getEachFitGroupVotes()
    }

    override suspend fun getFitMateList(): Response<FitGroupDetail> {
        return groupService.getFitMateList()
    }

    override suspend fun getFitMateProgress(): Response<FitGroupProgress> {
        return groupService.getFitMateProgress()
    }

    override suspend fun registerVote(voteRequest: VoteRequest): Response<VoteResponse> {
        return groupService.registerVote(voteRequest)
    }
}