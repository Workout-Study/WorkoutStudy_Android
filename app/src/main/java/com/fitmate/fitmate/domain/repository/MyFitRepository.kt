package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.FitRecordHistoryResponse
import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import retrofit2.Response
import retrofit2.http.Query

interface MyFitRepository {
    suspend fun getMyFitProgress(requestUserId: String): Response<MyFitProgressResponseDto>

    suspend fun getMyFitRecordHistory(
        userId: String,
        startDate: String,
        endDate: String,
    ): Response<FitRecordHistoryResponse>
}