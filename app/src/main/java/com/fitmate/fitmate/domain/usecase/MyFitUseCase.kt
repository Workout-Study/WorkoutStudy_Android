package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.FitRecordHistoryResponse
import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import com.fitmate.fitmate.domain.repository.MyFitRepository
import retrofit2.Response
import javax.inject.Inject

class MyFitUseCase @Inject constructor(
    private val myFitRepository: MyFitRepository,
) {
    suspend fun getMyFitProgress(userId: String): Response<MyFitProgressResponseDto> =
        myFitRepository.getMyFitProgress(userId)

    suspend fun getMyFitRecordHistory(
        userId: String,
        startDate: String,
        endDate: String,
    ): Response<FitRecordHistoryResponse> =
        myFitRepository.getMyFitRecordHistory(userId, startDate, endDate)

}