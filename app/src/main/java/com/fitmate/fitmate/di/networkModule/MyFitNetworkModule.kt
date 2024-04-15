package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.data.repository.CertificationRecordRepositoryImpl
import com.fitmate.fitmate.data.repository.MyFitRepositoryImpl
import com.fitmate.fitmate.data.source.remote.CertificationRecordService
import com.fitmate.fitmate.data.source.remote.CertificationTargetGroupService
import com.fitmate.fitmate.data.source.remote.MyFitService
import com.fitmate.fitmate.domain.repository.CertificationRecordRepository
import com.fitmate.fitmate.domain.repository.MyFitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyFitNetworkModule {

    //그룹별 내 인증 진척도 서비스 싱글톤
    @Provides
    @Singleton
    fun provideMyFitService(retrofit: Retrofit): MyFitService {
        /*val fitProgressBaseUrl = "http://43.202.247.71:8080/"*/
        val fitProgressBaseUrl = "https://236bb035-58bf-4127-ac62-e874502a9391.mock.pstmn.io/"
        return retrofit.newBuilder()
            .baseUrl(fitProgressBaseUrl)
            .build()
            .create(MyFitService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyFitRepository(myFitService: MyFitService): MyFitRepository =
        MyFitRepositoryImpl(myFitService)
}