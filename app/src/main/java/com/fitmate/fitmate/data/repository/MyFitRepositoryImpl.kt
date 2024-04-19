package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.dto.FitRecordHistoryResponse
import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import com.fitmate.fitmate.data.source.remote.MyFitService
import com.fitmate.fitmate.domain.repository.MyFitRepository
import retrofit2.Response
import javax.inject.Inject

class MyFitRepositoryImpl @Inject constructor(
    private val myFitService: MyFitService
): MyFitRepository {

    override suspend fun getMyFitProgress(requestUserId: String): Response<MyFitProgressResponseDto> {
        return myFitService.getMyFitProgress(requestUserId)
    }

    override suspend fun getMyFitRecordHistory(
        userId: String,
        startDate: String,
        endDate: String,
    ): Response<FitRecordHistoryResponse> {
        return myFitService.getMyFitRecordHistory(userId, startDate, endDate)
    }

}