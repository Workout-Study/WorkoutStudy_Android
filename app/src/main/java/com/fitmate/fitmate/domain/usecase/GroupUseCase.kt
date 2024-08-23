package com.fitmate.fitmate.domain.usecase

import androidx.paging.PagingData
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupFilter
import com.fitmate.fitmate.data.model.dto.FitMateKickRequestUserIdDto
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.RegisterResponse
import com.fitmate.fitmate.data.model.dto.ResponseFitMateKickDto
import com.fitmate.fitmate.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import javax.inject.Inject

class GroupUseCase @Inject constructor(private val groupRepository: GroupRepository) {

    suspend fun fitGroupFilter(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int)
    : Flow<PagingData<FitGroupDetail>> = groupRepository.fitGroupFilter(withMaxGroup, category, pageNumber, pageSize)

    suspend fun fitGroupAll(withMaxGroup: Boolean, pageNumber: Int, pageSize: Int): Flow<PagingData<FitGroupDetail>> = groupRepository.fitGroupAll(withMaxGroup, pageNumber, pageSize)

    suspend fun getFitGroupDetail(fitGroupId: Int): GetFitGroupDetail = groupRepository.getFitGroupDetail(fitGroupId)

    suspend fun getFitMateList(fitGroupId: Int): Response<GetFitMateList> = groupRepository.getFitMateList(fitGroupId)

    suspend fun registerFitMate(requestUserId: Int, fitGroupId: Int): RegisterResponse = groupRepository.registerFitMate(requestUserId, fitGroupId)

    suspend fun kickFitMate(fitGroupId: Int, userId: Int, requestUserId: FitMateKickRequestUserIdDto) = groupRepository.kickFitMate(fitGroupId, userId, requestUserId)
}