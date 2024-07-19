package com.fitmate.fitmate.domain.usecase

import androidx.paging.PagingData
import com.fitmate.fitmate.data.model.dto.PointDto
import com.fitmate.fitmate.data.model.dto.PointHistoryContentDto
import com.fitmate.fitmate.domain.repository.PointRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class PointUseCase @Inject constructor(private val pointRepository: PointRepository) {

    suspend fun getPointInfo(
        pointOwnerId: Int,
        pointOwnerType: String, ): Response<PointDto> = pointRepository.getPointInfo(pointOwnerId,pointOwnerType)

    suspend fun getPointHistory(
        pointOwnerId: Int,
        pointOwnerType: String,
        historyStartDate: String,
        historyEndDate: String,
        pageNumber: Int,
        pageSize: Int,
        tradeType: String,
    ): Flow<PagingData<PointHistoryContentDto>> = pointRepository.getPointHistory(pointOwnerId, pointOwnerType, historyStartDate, historyEndDate, pageNumber, pageSize, tradeType)
}