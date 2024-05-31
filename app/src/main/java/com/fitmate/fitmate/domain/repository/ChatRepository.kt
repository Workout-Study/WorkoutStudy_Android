package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.ChatResponse
import com.fitmate.fitmate.data.model.dto.FitGroup
import com.fitmate.fitmate.data.model.dto.RetrieveFitGroup
import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.data.source.ChatDatabase
import com.fitmate.fitmate.domain.model.ChatItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ChatRepository {

    fun load(): Flow<List<ChatItem>>

    suspend fun insert(item: ChatItem): Boolean

    suspend fun update(item: ChatItem): Boolean

    suspend fun getLastChatItem(): ChatEntity

    suspend fun getChatItemsByFitGroupId(fitGroupId: Int): List<ChatItem>

    suspend fun retrieveFitGroup(userId: String): Response<List<RetrieveFitGroup>>

    suspend fun retrieveMessage(messageId: String, fitGroupId: Int, fitMateId: Int, messageTime: String, messageType: String
    ): Response<ChatResponse>
}