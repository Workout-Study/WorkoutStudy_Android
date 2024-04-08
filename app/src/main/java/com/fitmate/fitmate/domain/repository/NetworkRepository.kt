package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.FitGroup
import com.fitmate.fitmate.data.model.dto.FitGroupResponse
import retrofit2.Call
import retrofit2.Response

interface NetworkRepository {
    suspend fun retrieveFitGroup(fitMateId: Int): Response<List<FitGroup>>

    suspend fun retrieveMessage(
        messageId: String,
        fitGroupId: String,
        fitMateId: String,
        messageTime: String,
        messageType: String
    ): List<String>
}