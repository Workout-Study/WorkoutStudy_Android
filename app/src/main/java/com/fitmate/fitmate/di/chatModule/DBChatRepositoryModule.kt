package com.fitmate.fitmate.di.chatModule

import com.fitmate.fitmate.data.repository.ChatRepositoryImpl
import com.fitmate.fitmate.data.source.ChatDatabase
import com.fitmate.fitmate.data.source.dao.ChatDao
import com.fitmate.fitmate.data.source.remote.ChatService
import com.fitmate.fitmate.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBChatRepositoryModule {

    @Singleton
    @Provides
    fun providesContentRepository(
        chatDao: ChatDao, chatDatabase: ChatDatabase, chatService: ChatService
    ): ChatRepository = ChatRepositoryImpl(chatDao, chatDatabase, chatService)

}