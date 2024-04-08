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

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http:3.38.227.26:8080/")
        .client(okHttpClientInterceptor)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val logging = HttpLoggingInterceptor().apply {
        // 요청과 응답의 본문 내용까지 로그에 포함
        level = HttpLoggingInterceptor.Level.BODY
    }
    //OKHttp Logging Interceptor
    private val okHttpClientInterceptor = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    @Provides
    @Singleton
    fun provideFitGroupService(retrofit: Retrofit): NetworkDao = retrofit.create(NetworkDao::class.java)

    @Provides
    @Singleton
    fun providesContentRepository(networkDao: NetworkDao): NetworkRepository = NetworkRepositoryImpl(networkDao)
}
