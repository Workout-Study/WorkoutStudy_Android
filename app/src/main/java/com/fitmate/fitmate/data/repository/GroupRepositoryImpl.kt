package com.fitmate.fitmate.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fitmate.fitmate.data.model.dto.FitGroupFilter
import com.fitmate.fitmate.data.model.dto.FitMateKickRequestUserIdDto
import com.fitmate.fitmate.data.model.dto.FitMateRequest
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.RegisterResponse
import com.fitmate.fitmate.data.model.dto.ResponseFitMateKickDto
import com.fitmate.fitmate.data.source.CategoryPagingSource
import com.fitmate.fitmate.data.source.remote.GroupService
import com.fitmate.fitmate.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class GroupRepositoryImpl(private val groupService: GroupService) : GroupRepository {
    override suspend fun fitGroupFilter(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int) = Pager(
        config = PagingConfig(
            pageSize,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            CategoryPagingSource(groupService,category,pageSize)
        }
    ).flow

    override suspend fun fitGroupAll(withMaxGroup: Boolean, pageNumber: Int ,pageSize: Int): Flow<FitGroupFilter> {
        return flow {
            emit(groupService.fitGroupAll(false, pageNumber ,pageSize))
        }
    }

    override suspend fun getFitGroupDetail(fitGroupId: Int): GetFitGroupDetail {
        return groupService.getFitGroupDetail(fitGroupId)
    }

    override suspend fun getFitMateList(fitGroupId: Int): Response<GetFitMateList> {
        return groupService.getFitMateList(fitGroupId)
    }

    override suspend fun registerFitMate(requestUserId: Int, fitGroupId: Int): RegisterResponse {
        return groupService.registerFitMate(FitMateRequest(requestUserId, fitGroupId))
    }

    override suspend fun kickFitMate(
        fitGroupId: Int,
        userId: Int,
        requestUserId: FitMateKickRequestUserIdDto,
    ): Response<ResponseFitMateKickDto> {
        return groupService.kickFitMate(fitGroupId, userId, requestUserId)
    }

}