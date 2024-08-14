package com.fitmate.fitmate.di.networkModule

import com.fitmate.fitmate.data.repository.FitOffRepositoryImpl
import com.fitmate.fitmate.data.source.remote.FitOffService
import com.fitmate.fitmate.domain.repository.FitOffRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FitOffModule {

    @Provides
    @Singleton
    fun provideFitOffService(retrofit:Retrofit):FitOffService{
        return retrofit.create(FitOffService::class.java)
    }

    @Provides
    @Singleton
    fun providesFitOffRepository(fitOffService: FitOffService): FitOffRepository = FitOffRepositoryImpl(fitOffService)
}