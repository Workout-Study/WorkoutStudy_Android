package com.fitmate.fitmate.data.source.dao

import com.fitmate.fitmate.data.model.dto.ChatResponse
import com.fitmate.fitmate.data.model.dto.FitGroup
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatService {
    @GET("retrieve/fit-group/")
    suspend fun retrieveFitGroup(@Query("fitMateId") fitMateId: Int): Response<List<FitGroup>>

    @GET("retrieve/message/")
    suspend fun retrieveMessage(
        @Query("messageId") messageId: String,
        @Query("fitGroupId") fitGroupId: Int,
        @Query("fitMateId") fitMateId: Int,
        @Query("messageTime") messageTime: String,
        @Query("messageType") messageType: String
    ): Response<ChatResponse>
}