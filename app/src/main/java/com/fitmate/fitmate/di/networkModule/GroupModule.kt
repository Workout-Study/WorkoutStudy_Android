package com.fitmate.fitmate.di.networkModule

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
        // val fitGroupBaseUrl = "http://43.202.247.71:8080"
        val fitGroupBaseUrl = "https://run.mocky.io/v3/"
        return retrofit.newBuilder()
            .baseUrl(fitGroupBaseUrl)
            .build()
            .create(GroupService::class.java)
    }

    @Provides
    @Singleton
    fun providesContentRepository(groupService: GroupService): GroupRepository = GroupRepositoryImpl(groupService)
}
