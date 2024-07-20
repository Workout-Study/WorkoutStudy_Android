package com.fitmate.fitmate.domain.repository

import androidx.paging.PagingData
import com.fitmate.fitmate.data.model.dto.PointDto
import com.fitmate.fitmate.data.model.dto.PointHistoryContentDto
import com.fitmate.fitmate.data.source.remote.PointService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface PointRepository {
    suspend fun getPointInfo(pointOwnerId: Int, pointOwnerType: String): Response<PointDto>

    suspend fun getPointHistory(
        pointOwnerId: Int,
        pointOwnerType: String,
        historyStartDate: String?,
        historyEndDate: String?,
        pageNumber: Int,
        pageSize: Int,
        tradeType: String?,
    ): Flow<PagingData<PointHistoryContentDto>>
}