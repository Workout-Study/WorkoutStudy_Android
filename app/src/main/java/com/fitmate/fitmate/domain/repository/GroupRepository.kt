package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.data.model.dto.VoteResponse
import retrofit2.Response

interface GroupRepository {
    suspend fun getFilteredFitGroups(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int): GroupResponse

    suspend fun getFitGroupDetail(fitGroupId: Int): GroupDetailResponse

    suspend fun myFitGroupVotes(): Result<List<MyFitGroupVote>>

    suspend fun getEachGroupVotes(): Response<EachFitResponse>

    suspend fun getFitMateList(): Response<FitGroupDetail>

    suspend fun getFitMateProgress(): Response<FitGroupProgress>

    suspend fun registerVote(voteRequest: VoteRequest): Response<VoteResponse>
}