package com.fitmate.fitmate.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import com.fitmate.fitmate.data.source.MyGroupNewsPagingSource
import com.fitmate.fitmate.data.source.remote.CertificationTargetGroupService
import com.fitmate.fitmate.data.source.remote.MyGroupNewsService
import com.fitmate.fitmate.domain.repository.MyGroupNewsRepository
import retrofit2.Response
import javax.inject.Inject

class MyGroupNewsRepositoryImpl @Inject constructor(private val myGroupService: CertificationTargetGroupService, private val myGroupNewsService: MyGroupNewsService) :
    MyGroupNewsRepository {

    override suspend fun getMyFitGroupList(userId: Int): Response<CertificationTargetFitGroupResponseDto> {
        return myGroupService.getTargetGroupList(userId)
    }

    override suspend fun getMyGroupNews(
        userId: Int,
        pageNumber: Int,
        pageSize: Int,
    ) = Pager(
        config = PagingConfig(
            pageSize,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            MyGroupNewsPagingSource(
                myGroupNewsService,
                userId,
                pageNumber,
                pageSize,
                )
        }
    ).flow
}