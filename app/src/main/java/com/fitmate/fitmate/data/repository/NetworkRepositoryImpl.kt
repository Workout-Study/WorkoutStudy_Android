package com.fitmate.fitmate.data.repository

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
        fitGroupId: String,
        fitMateId: String,
        messageTime: String,
        messageType: String
    ): List<String> = networkDao.retrieveMessage(messageId, fitGroupId, fitMateId, messageTime, messageType)
}