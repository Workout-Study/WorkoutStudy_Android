package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.data.repository.GroupRepositoryImpl
import com.fitmate.fitmate.data.source.remote.GroupService
import com.fitmate.fitmate.domain.repository.GroupRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GroupModule {
    @Provides
    @Singleton
    fun provideGroupService(retrofit: Retrofit): GroupService {
        val fitGroupBaseUrl = "http://${BuildConfig.CHAT_SERVER_ADDRESS}:8080"
        return retrofit.newBuilder()
            .baseUrl(fitGroupBaseUrl)
            .build()
            .create(GroupService::class.java)
    }
    @Provides
    @Singleton
    fun providesContentRepository(groupService: GroupService): GroupRepository = GroupRepositoryImpl(groupService)
}
