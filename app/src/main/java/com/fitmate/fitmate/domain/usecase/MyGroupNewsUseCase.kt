package com.fitmate.fitmate.domain.usecase

import androidx.paging.PagingData
import com.fitmate.fitmate.data.model.dto.MyGroupNewsDto
import com.fitmate.fitmate.domain.repository.MyGroupNewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyGroupNewsUseCase @Inject constructor(private val myGroupNewsRepository: MyGroupNewsRepository) {

    suspend fun getMyGroupNews(
        userId: Int,
        pageNumber: Int,
        pageSize: Int,
    ): Flow<PagingData<MyGroupNewsDto>> = myGroupNewsRepository.getMyGroupNews(userId, pageNumber, pageSize)

}