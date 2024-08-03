package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.MyGroupNewsResponseDto
import retrofit2.http.Query

interface MyGroupNewsService {

    suspend fun getGroupNews(
        @Query("userId") userId: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
    ): MyGroupNewsResponseDto

}