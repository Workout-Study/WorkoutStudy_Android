package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.data.repository.CertificationRecordRepositoryImpl
import com.fitmate.fitmate.data.repository.NetworkRepositoryImpl
import com.fitmate.fitmate.data.source.dao.ChatService
import com.fitmate.fitmate.data.source.remote.CertificationRecordService
import com.fitmate.fitmate.data.source.remote.CertificationTargetGroupService
import com.fitmate.fitmate.domain.repository.CertificationRecordRepository
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
object CertificationNetworkModule {

    //기록 레트로핏 서비스 싱글톤
    @Provides
    @Singleton
    fun provideCertificationRecordService(retrofit: Retrofit): CertificationRecordService =
        retrofit.create(CertificationRecordService::class.java)

    //기록 레포지토리 싱글톤
    @Provides
    @Singleton
    fun providesCertificationRecordRepository(certificationRecordService: CertificationRecordService, certificationTargetGroupService: CertificationTargetGroupService): CertificationRecordRepository =
        CertificationRecordRepositoryImpl(certificationRecordService,certificationTargetGroupService)

    //내가 가입한 그룹 GET 서비스 싱글톤
    @Provides
    @Singleton
    fun provideFitGroupService(retrofit: Retrofit): CertificationTargetGroupService {
        /*val fitGroupBaseUrl = "http://43.202.247.71:8080/"*/
        val fitGroupBaseUrl = "https://712ead16-f246-44a1-b597-d7f1faf8906d.mock.pstmn.io/"
        return retrofit.newBuilder()
            .baseUrl(fitGroupBaseUrl)
            .build()
            .create(CertificationTargetGroupService::class.java)
    }


}