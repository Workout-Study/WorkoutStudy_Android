package com.fitmate.fitmate.domain.repository

import androidx.paging.PagingData
import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import com.fitmate.fitmate.data.model.dto.MyGroupNewsDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MyGroupNewsRepository {

    suspend fun getMyFitGroupList(userId:Int): Response<CertificationTargetFitGroupResponseDto>

    suspend fun getMyGroupNews(userId:Int, pageNumber:Int, pageSize:Int): Flow<PagingData<MyGroupNewsDto>>
}