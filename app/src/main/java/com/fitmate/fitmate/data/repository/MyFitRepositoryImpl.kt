package com.fitmate.fitmate.data.repository

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

}