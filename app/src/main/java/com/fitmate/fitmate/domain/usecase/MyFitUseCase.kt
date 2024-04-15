package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import com.fitmate.fitmate.domain.repository.MyFitRepository
import retrofit2.Response
import javax.inject.Inject

class MyFitUseCase @Inject constructor(
    private val myFitRepository: MyFitRepository
) {
    suspend fun getMyFitProgress(userId:String): Response<MyFitProgressResponseDto> = myFitRepository.getMyFitProgress(userId)
}