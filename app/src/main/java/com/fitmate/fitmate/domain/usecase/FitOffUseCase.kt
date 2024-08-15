package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.FitOffListDto
import com.fitmate.fitmate.data.model.dto.FitOffRequestDto
import com.fitmate.fitmate.data.model.dto.FitOffResponseDto
import com.fitmate.fitmate.domain.repository.FitOffRepository
import com.fitmate.fitmate.domain.repository.MyFitRepository
import retrofit2.Response
import javax.inject.Inject

class FitOffUseCase @Inject constructor(
    private val fitOffRepository: FitOffRepository,
) {
    suspend fun registerFitOff(fitOffItem: FitOffRequestDto): Response<FitOffResponseDto> = fitOffRepository.registerFitOff(fitOffItem)

    suspend fun getFitOffByUserId(userId: Int): Response<FitOffListDto> = fitOffRepository.getFitOffByUserId(userId)

    suspend fun getFitOffByGroupId(groupId: Int): Response<FitOffListDto> = fitOffRepository.getFitOffByGroupId(groupId)
}