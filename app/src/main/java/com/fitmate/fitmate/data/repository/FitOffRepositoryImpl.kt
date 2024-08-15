package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.dto.FitOffListDto
import com.fitmate.fitmate.data.model.dto.FitOffRequestDto
import com.fitmate.fitmate.data.model.dto.FitOffResponseDto
import com.fitmate.fitmate.data.source.remote.FitOffService
import com.fitmate.fitmate.domain.repository.FitOffRepository
import retrofit2.Response
import javax.inject.Inject

class FitOffRepositoryImpl@Inject constructor(
    private val fitOffService: FitOffService
):FitOffRepository {
    override suspend fun registerFitOff(fitOffItem: FitOffRequestDto): Response<FitOffResponseDto> {
        return fitOffService.registerFitOff(fitOffItem)
    }

    override suspend fun getFitOffByUserId(userId: Int): Response<FitOffListDto> {
        return fitOffService.getFitOffByUserId(userId)
    }

    override suspend fun getFitOffByGroupId(groupId: Int): Response<FitOffListDto> {
        return fitOffService.getFitOffByGroupId(groupId)
    }
}