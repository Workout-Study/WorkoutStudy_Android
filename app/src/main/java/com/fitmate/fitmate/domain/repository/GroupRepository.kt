package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.FitGroupFilter
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.RegisterResponse
import retrofit2.Response

interface GroupRepository {
    suspend fun fitGroupFilter(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int): FitGroupFilter

    suspend fun fitGroupAll(withMaxGroup: Boolean): FitGroupFilter

    suspend fun getFitGroupDetail(fitGroupId: Int): GetFitGroupDetail

    suspend fun getFitMateList(fitGroupId: Int): Response<GetFitMateList>

    suspend fun registerFitMate(requestUserId: Int, fitGroupId: Int): RegisterResponse
}