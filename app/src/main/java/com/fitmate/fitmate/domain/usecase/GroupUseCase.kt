package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.FitGroupFilter
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.RegisterResponse
import com.fitmate.fitmate.domain.repository.GroupRepository
import retrofit2.Response
import javax.inject.Inject

class GroupUseCase @Inject constructor(private val groupRepository: GroupRepository) {

    suspend fun fitGroupFilter(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int)
    : FitGroupFilter = groupRepository.fitGroupFilter(withMaxGroup, category, pageNumber, pageSize)

    suspend fun fitGroupAll(withMaxGroup: Boolean, pageSize: Int): FitGroupFilter = groupRepository.fitGroupAll(withMaxGroup, pageSize)

    suspend fun getFitGroupDetail(fitGroupId: Int): GetFitGroupDetail = groupRepository.getFitGroupDetail(fitGroupId)

    suspend fun getFitMateList(fitGroupId: Int): Response<GetFitMateList> = groupRepository.getFitMateList(fitGroupId)

    suspend fun registerFitMate(requestUserId: Int, fitGroupId: Int): RegisterResponse = groupRepository.registerFitMate(requestUserId, fitGroupId)
}