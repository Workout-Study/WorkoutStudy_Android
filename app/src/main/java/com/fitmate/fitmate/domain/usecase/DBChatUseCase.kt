package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.ChatResponse
import com.fitmate.fitmate.data.model.dto.FitGroup
import com.fitmate.fitmate.data.model.dto.RetrieveFitGroup
import com.fitmate.fitmate.domain.model.ChatItem
import com.fitmate.fitmate.domain.repository.ChatRepository
import retrofit2.Response
import javax.inject.Inject

class DBChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
){

    fun load() = chatRepository.load()

    suspend fun insert(item: ChatItem) = chatRepository.insert(item)

    suspend fun update(item: ChatItem) = chatRepository.update(item)

    suspend fun getLastChatItem() = chatRepository.getLastChatItem()

    suspend fun getChatItemsByFitGroupId(fitGroupId: Int) = chatRepository.getChatItemsByFitGroupId(fitGroupId)

    suspend fun retrieveFitGroup(userId: String): Response<List<RetrieveFitGroup>> = chatRepository.retrieveFitGroup(userId)

    suspend fun retrieveMessage(
        messageId: String,
        fitGroupId: Int,
        fitMateId: Int,
        messageTime: String,
        messageType: String
    ): Response<ChatResponse> = chatRepository.retrieveMessage(messageId, fitGroupId, fitMateId, messageTime, messageType)

}