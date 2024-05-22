package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.data.source.dao.ChatService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    //채팅 레트로핏 서비스 싱글톤
    @Provides
    @Singleton
    fun provideFitGroupService(retrofit: Retrofit): ChatService {
        val fitGroupBaseUrl = "http://${BuildConfig.SERVER_IP}:8888/"
        return retrofit.newBuilder()
            .baseUrl(fitGroupBaseUrl)
            .build()
            .create(ChatService::class.java)
    }

    //채팅 레포지토리 싱글톤
//    @Provides
//    @Singleton
//    fun providesContentRepository(chatDao: ChatDao, chatDatabase: ChatDatabase, chatService: ChatService): ChatRepository = ChatRepositoryImpl(chatDao, chatDatabase, chatService)
}

