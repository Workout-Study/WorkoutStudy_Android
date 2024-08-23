package com.fitmate.fitmate.domain.repository

import androidx.paging.PagingData
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupFilter
import com.fitmate.fitmate.data.model.dto.FitMateKickRequestUserIdDto
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.RegisterResponse
import com.fitmate.fitmate.data.model.dto.ResponseFitMateKickDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

interface GroupRepository {
    suspend fun fitGroupFilter(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int): Flow<PagingData<FitGroupDetail>>

    suspend fun fitGroupAll(withMaxGroup: Boolean, pageNumber: Int ,pageSize: Int): Flow<PagingData<FitGroupDetail>>

    suspend fun getFitGroupDetail(fitGroupId: Int): GetFitGroupDetail

    suspend fun getFitMateList(fitGroupId: Int): Response<GetFitMateList>

    suspend fun registerFitMate(requestUserId: Int, fitGroupId: Int): RegisterResponse

    suspend fun kickFitMate(fitGroupId: Int, userId: Int, requestUserId: FitMateKickRequestUserIdDto): Response<ResponseFitMateKickDto>
}