package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.data.repository.VoteRepositoryImpl
import com.fitmate.fitmate.data.source.remote.VoteService
import com.fitmate.fitmate.domain.repository.VoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VoteModule {

    @Provides
    @Singleton
    fun provideVoteService(retrofit: Retrofit): VoteService {
        val voteBaseUrl = "http://${BuildConfig.CHAT_SERVER_ADDRESS}:8081"
        return retrofit.newBuilder()
            .baseUrl(voteBaseUrl)
            .build()
            .create(VoteService::class.java)
    }

    @Provides
    @Singleton
    fun providesContentRepository(voteService: VoteService): VoteRepository = VoteRepositoryImpl(voteService)
}