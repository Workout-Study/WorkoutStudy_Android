package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.domain.model.ChatItem
import com.fitmate.fitmate.domain.repository.ChatRepository
import javax.inject.Inject

class DBChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
){

    fun load() = chatRepository.load()

    suspend fun insert(item: ChatItem) = chatRepository.insert(item)

    suspend fun update(item: ChatItem) = chatRepository.update(item)

}