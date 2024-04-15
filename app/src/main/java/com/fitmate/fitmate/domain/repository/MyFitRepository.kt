package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import retrofit2.Response

interface MyFitRepository {
   suspend fun getMyFitProgress(requestUserId: String): Response<MyFitProgressResponseDto>
}