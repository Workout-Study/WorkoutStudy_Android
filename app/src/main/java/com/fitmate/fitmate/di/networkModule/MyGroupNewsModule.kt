package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.data.repository.MyGroupNewsRepositoryImpl
import com.fitmate.fitmate.data.source.remote.MyGroupNewsService
import com.fitmate.fitmate.domain.repository.MyGroupNewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyGroupNewsModule {
    @Provides
    @Singleton
    fun provideMyGroupNewsService(retrofit: Retrofit): MyGroupNewsService {
        val pointBaseUrl = "http://${BuildConfig.SERVER_ADDRESS}:8081"
        return retrofit.newBuilder()
            .baseUrl(pointBaseUrl)
            .build()
            .create(MyGroupNewsService::class.java)
    }

    @Provides
    @Singleton
    fun providesMyGroupNewsRepository(myGroupNewsService: MyGroupNewsService): MyGroupNewsRepository = MyGroupNewsRepositoryImpl(myGroupNewsService)
}