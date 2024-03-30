package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.source.ChatDatabase
import com.fitmate.fitmate.domain.model.ChatItem
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    val chatDatabase: ChatDatabase

    fun load(): Flow<List<ChatItem>>

    suspend fun insert(item: ChatItem): Boolean

    suspend fun update(item: ChatItem): Boolean

}