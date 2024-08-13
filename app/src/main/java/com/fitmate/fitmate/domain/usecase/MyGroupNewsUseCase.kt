package com.fitmate.fitmate.domain.usecase

import androidx.paging.PagingData
import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import com.fitmate.fitmate.data.model.dto.MyGroupNewsDto
import com.fitmate.fitmate.domain.repository.MyGroupNewsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MyGroupNewsUseCase @Inject constructor(private val myGroupNewsRepository: MyGroupNewsRepository) {

    suspend fun getMyFitGroupList(userId:Int): Response<CertificationTargetFitGroupResponseDto> = myGroupNewsRepository.getMyFitGroupList(userId)

    suspend fun getMyGroupNews(
        userId: Int,
        pageNumber: Int,
        pageSize: Int,
    ): Flow<PagingData<MyGroupNewsDto>> = myGroupNewsRepository.getMyGroupNews(userId, pageNumber, pageSize)

}