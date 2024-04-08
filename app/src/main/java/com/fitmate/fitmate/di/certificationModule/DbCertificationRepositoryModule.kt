package com.fitmate.fitmate.di.certificationModule

import com.fitmate.fitmate.data.repository.CertificationRepositoryImpl
import com.fitmate.fitmate.data.source.dao.CertificationDao
import com.fitmate.fitmate.domain.repository.CertificationRepository
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbCertificationRepositoryModule {

    @Singleton
    @Provides
    fun providesContentRepository(
        certificationDao: CertificationDao,
        storageReference: StorageReference
    ):CertificationRepository = CertificationRepositoryImpl(certificationDao,storageReference)

}