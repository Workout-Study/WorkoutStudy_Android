package com.fitmate.fitmate.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fitmate.fitmate.data.model.dto.FitGroupFilter
import com.fitmate.fitmate.data.model.dto.FitMateRequest
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.RegisterResponse
import com.fitmate.fitmate.data.source.CategoryPagingSource
import com.fitmate.fitmate.data.source.remote.GroupService
import com.fitmate.fitmate.domain.repository.GroupRepository
import retrofit2.Response

class GroupRepositoryImpl(private val groupService: GroupService) : GroupRepository {
    override suspend fun fitGroupFilter(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int) = Pager(
        config = PagingConfig(
            pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            CategoryPagingSource(groupService,category,pageSize)
        }
    ).flow

    override suspend fun fitGroupAll(withMaxGroup: Boolean, pageNumber: Int ,pageSize: Int) = Pager(
        config = PagingConfig(
            pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            CategoryPagingSource(groupService, 0, pageSize)
        }
    ).flow

    override suspend fun getFitGroupDetail(fitGroupId: Int): GetFitGroupDetail {
        return groupService.getFitGroupDetail(fitGroupId)
    }

    override suspend fun getFitMateList(fitGroupId: Int): Response<GetFitMateList> {
        return groupService.getFitMateList(fitGroupId)
    }

    override suspend fun registerFitMate(requestUserId: Int, fitGroupId: Int): RegisterResponse {
        return groupService.registerFitMate(FitMateRequest(requestUserId, fitGroupId))
    }

}