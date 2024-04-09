package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.ChatResponse
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
        fitGroupId: Int,
        fitMateId: Int,
        messageTime: String,
        messageType: String
    ): Response<ChatResponse> = networkRepository.retrieveMessage(messageId, fitGroupId, fitMateId, messageTime, messageType)
}