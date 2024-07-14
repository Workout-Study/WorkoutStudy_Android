package com.fitmate.fitmate.di.networkModule

import android.content.Context
import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.data.repository.MakeFitGroupRepositoryImpl
import com.fitmate.fitmate.data.source.remote.RegisterFitGroupService
import com.fitmate.fitmate.domain.repository.MakeFitGroupRepository
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MakeFitGroupNetworkModule {

    @Singleton
    @Provides
    fun provideRegisterFitGroupService(retrofit:Retrofit): RegisterFitGroupService {
        val makeGroupBaseUrl = "http://${BuildConfig.SERVER_ADDRESS}:8080/"
        return retrofit.newBuilder()
            .baseUrl(makeGroupBaseUrl)
            .build()
            .create(RegisterFitGroupService::class.java)
    }

    @Singleton
    @Provides
    fun provideMakeFitGroupRepository(
        registerFitGroupService: RegisterFitGroupService,
        storageReference: StorageReference,
        @ApplicationContext context: Context
    ): MakeFitGroupRepository = MakeFitGroupRepositoryImpl(registerFitGroupService, storageReference, context)
}