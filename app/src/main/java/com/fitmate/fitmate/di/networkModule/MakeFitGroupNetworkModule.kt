package com.fitmate.fitmate.di.networkModule

import android.content.Context
import com.fitmate.fitmate.data.repository.MakeFitGroupRepositoryImpl
import com.fitmate.fitmate.domain.repository.MakeFitGroupRepository
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MakeFitGroupNetworkModule {

    @Singleton
    @Provides
    fun provideMakeFitGroupRepository(
        storageReference: StorageReference,
        @ApplicationContext context: Context
    ): MakeFitGroupRepository = MakeFitGroupRepositoryImpl(storageReference,context)
}