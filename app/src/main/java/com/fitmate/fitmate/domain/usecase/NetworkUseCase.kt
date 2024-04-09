package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.FitGroup
import com.fitmate.fitmate.domain.repository.NetworkRepository
import retrofit2.Response
import javax.inject.Inject

class NetworkUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend fun retrieveFitGroup(fitMateId: Int): Response<List<FitGroup>> = networkRepository.retrieveFitGroup(fitMateId)

    suspend fun retrieveMessage(
        messageId: String,
        fitGroupId: String,
        fitMateId: String,
        messageTime: String,
        messageType: String
    ): List<String> = networkRepository.retrieveMessage(messageId, fitGroupId, fitMateId, messageTime, messageType)
}