package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.data.repository.GroupRepositoryImpl
import com.fitmate.fitmate.data.repository.PointRepositoryImpl
import com.fitmate.fitmate.data.source.remote.GroupService
import com.fitmate.fitmate.data.source.remote.PointService
import com.fitmate.fitmate.domain.repository.GroupRepository
import com.fitmate.fitmate.domain.repository.PointRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PointModule {
    @Provides
    @Singleton
    fun providePointService(retrofit: Retrofit): PointService {
        val pointBaseUrl = "http://${BuildConfig.SERVER_ADDRESS}:8082"
        return retrofit.newBuilder()
            .baseUrl(pointBaseUrl)
            .build()
            .create(PointService::class.java)
    }

    @Provides
    @Singleton
    fun providesPointRepository(pointService: PointService): PointRepository = PointRepositoryImpl(pointService)
}