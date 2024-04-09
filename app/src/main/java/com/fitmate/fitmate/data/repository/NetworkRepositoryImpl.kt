package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.dto.ChatResponse
import com.fitmate.fitmate.data.model.dto.FitGroup
import com.fitmate.fitmate.data.source.dao.NetworkDao
import com.fitmate.fitmate.domain.repository.NetworkRepository
import retrofit2.Response
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val networkDao: NetworkDao
): NetworkRepository {
    override suspend fun retrieveFitGroup(fitMateId: Int): Response<List<FitGroup>> = networkDao.retrieveFitGroup(fitMateId)

    override suspend fun retrieveMessage(
        messageId: String,
        fitGroupId: Int,
        fitMateId: Int,
        messageTime: String,
        messageType: String
    ): Response<ChatResponse> = networkDao.retrieveMessage(messageId, fitGroupId, fitMateId, messageTime, messageType)
}