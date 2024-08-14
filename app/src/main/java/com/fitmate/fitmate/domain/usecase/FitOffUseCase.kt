package com.fitmate.fitmate.domain.usecase

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
}