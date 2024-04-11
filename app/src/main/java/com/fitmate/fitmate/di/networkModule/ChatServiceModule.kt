package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.data.repository.NetworkRepositoryImpl
import com.fitmate.fitmate.data.source.dao.ChatService
import com.fitmate.fitmate.domain.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //채팅 레트로핏 서비스 싱글톤
    @Provides
    @Singleton
    fun provideFitGroupService(retrofit: Retrofit): ChatService {
        val fitGroupBaseUrl = "http://3.38.227.26:8080/"
        return retrofit.newBuilder()
            .baseUrl(fitGroupBaseUrl)
            .build()
            .create(ChatService::class.java)
    }

    //채팅 레포지토리 싱글톤
    @Provides
    @Singleton
    fun providesContentRepository(chatService: ChatService): NetworkRepository = NetworkRepositoryImpl(chatService)
}
