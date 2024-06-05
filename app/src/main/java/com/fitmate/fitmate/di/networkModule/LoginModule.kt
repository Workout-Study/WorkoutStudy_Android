package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.data.repository.LoginRepositoryImpl
import com.fitmate.fitmate.data.source.remote.LoginService
import com.fitmate.fitmate.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService {
        val loginBaseUrl = "http://${BuildConfig.CHAT_SERVER_ADDRESS}:8084"
        return retrofit.newBuilder()
            .baseUrl(loginBaseUrl)
            .build()
            .create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun providesContentRepository(loginService: LoginService): LoginRepository = LoginRepositoryImpl(loginService)
}