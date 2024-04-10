package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.data.repository.NetworkRepositoryImpl
import com.fitmate.fitmate.data.source.dao.NetworkDao
import com.fitmate.fitmate.domain.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //채팅 레트로핏 서비스 싱글톤
    @Provides
    @Singleton
    fun provideFitGroupService(retrofit: Retrofit): NetworkDao {
        val fitGroupBaseUrl = "http://3.38.227.26:8080/"
        return retrofit.newBuilder()
            .baseUrl(fitGroupBaseUrl)
            .build()
            .create(NetworkDao::class.java)
    }

    //채팅 레포지토리 싱글톤
    @Provides
    @Singleton
    fun providesContentRepository(networkDao: NetworkDao): NetworkRepository = NetworkRepositoryImpl(networkDao)
}
