package com.fitmate.fitmate.domain.repository

import androidx.paging.PagingData
import com.fitmate.fitmate.data.model.dto.MyGroupNewsDto
import kotlinx.coroutines.flow.Flow

interface MyGroupNewsRepository {
    suspend fun getMyGroupNews(userId:Int, pageNumber:Int, pageSize:Int): Flow<PagingData<MyGroupNewsDto>>
}