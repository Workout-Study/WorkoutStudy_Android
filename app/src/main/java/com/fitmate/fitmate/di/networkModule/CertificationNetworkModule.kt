package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.data.repository.CertificationRecordRepositoryImpl
import com.fitmate.fitmate.data.source.remote.CertificationRecordService
import com.fitmate.fitmate.domain.repository.CertificationRecordRepository
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
object CertificationNetworkModule {

    @Singleton
    private var BASE_URL = "http://43.202.247.71:8081/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
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
    fun provideCertificationRecordService(retrofit: Retrofit):CertificationRecordService = retrofit.create(CertificationRecordService::class.java)

    @Provides
    @Singleton
    fun providesCertificationRecordRepository(certificationRecordService:CertificationRecordService): CertificationRecordRepository = CertificationRecordRepositoryImpl(certificationRecordService)
}