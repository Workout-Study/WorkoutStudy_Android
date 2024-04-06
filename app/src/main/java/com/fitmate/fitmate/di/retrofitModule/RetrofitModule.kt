package com.fitmate.fitmate.di.retrofitModule

import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.domain.FitGroupService.FitGroupService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    var provideBaseUrl = BuildConfig.CHAT_SERVER_ADDRESS

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("http://${provideBaseUrl}:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideFitGroupService(retrofit: Retrofit): FitGroupService {
        return retrofit.create(FitGroupService::class.java)
    }
}
