package com.fitmate.fitmate.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fitmate.fitmate.data.model.dto.PointDto
import com.fitmate.fitmate.data.source.PointHistoryPagingSource
import com.fitmate.fitmate.data.source.remote.PointService
import com.fitmate.fitmate.domain.repository.PointRepository
import retrofit2.Response

class PointRepositoryImpl(private val pointService: PointService) : PointRepository {
    override suspend fun getPointInfo(
        pointOwnerId: Int,
        pointOwnerType: String,
    ): Response<PointDto> {
        return pointService.getPointInfo(pointOwnerId, pointOwnerType)
    }

    override suspend fun getPointHistory(
        pointOwnerId: Int,
        pointOwnerType: String,
        historyStartDate: String,
        historyEndDate: String,
        pageNumber: Int,
        pageSize: Int,
        tradeType: String,
    ) = Pager(
        config = PagingConfig(
            pageSize,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            PointHistoryPagingSource(
                pointService,
                pointOwnerId,
                pointOwnerType,
                historyStartDate,
                historyEndDate,
                pageNumber,
                pageSize,
                tradeType)
        }
    ).flow
}