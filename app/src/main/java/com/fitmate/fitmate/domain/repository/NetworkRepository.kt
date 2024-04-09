package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.ChatResponse
import com.fitmate.fitmate.data.model.dto.FitGroup
import retrofit2.Call
import retrofit2.Response

interface NetworkRepository {
    suspend fun retrieveFitGroup(fitMateId: Int): Response<List<FitGroup>>

    suspend fun retrieveMessage(
        messageId: String,
        fitGroupId: Int,
        fitMateId: Int,
        messageTime: String,
        messageType: String
    ): Response<ChatResponse>
}