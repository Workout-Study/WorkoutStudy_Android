package com.fitmate.fitmate.di.storageModule

import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageReferencesModule {

    @Singleton
    @Provides
    fun provideStorageRef(): StorageReference {
        val storage = Firebase.storage
        return storage.reference
    }
}