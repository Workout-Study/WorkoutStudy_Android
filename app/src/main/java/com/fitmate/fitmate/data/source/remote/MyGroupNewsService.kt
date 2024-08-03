package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.MyGroupNewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MyGroupNewsService {

    @GET("/my-fit-service/my-fits/issue")
    suspend fun getGroupNews(
        @Query("userId") userId: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
    ): MyGroupNewsResponseDto

}