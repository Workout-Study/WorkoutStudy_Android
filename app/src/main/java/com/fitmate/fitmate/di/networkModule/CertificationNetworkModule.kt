package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.data.repository.CertificationRecordRepositoryImpl
import com.fitmate.fitmate.data.source.remote.CertificationRecordService
import com.fitmate.fitmate.data.source.remote.CertificationTargetGroupService
import com.fitmate.fitmate.domain.repository.CertificationRecordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
        val fitGroupBaseUrl = "https://cfcd4b3c-24e1-4275-bffe-66197bef03d2.mock.pstmn.io/"
        return retrofit.newBuilder()
            .baseUrl(fitGroupBaseUrl)
            .build()
            .create(CertificationTargetGroupService::class.java)
    }

}