package com.fitmate.fitmate.data.source.dao

import com.fitmate.fitmate.data.model.dto.ChatResponse
import com.fitmate.fitmate.data.model.dto.FitGroup
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkDao {
    @GET("retrieve/fit-group/")
    suspend fun retrieveFitGroup(@Query("fitMateId") fitMateId: Int): Response<List<FitGroup>>

    @GET("retrieve/message/")
    suspend fun retrieveMessage(
        @Query("message-id") messageId: String,
        @Query("fit-group-id") fitGroupId: Int,
        @Query("fit-mate-id") fitMateId: Int,
        @Query("message-time") messageTime: String,
        @Query("message-type") messageType: String
    ): Response<ChatResponse>
}