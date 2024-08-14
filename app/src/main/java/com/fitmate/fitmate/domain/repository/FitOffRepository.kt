package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.FitOffRequestDto
import com.fitmate.fitmate.data.model.dto.FitOffResponseDto
import retrofit2.Response

interface FitOffRepository {
    suspend fun registerFitOff(fitOffItem: FitOffRequestDto): Response<FitOffResponseDto>
}