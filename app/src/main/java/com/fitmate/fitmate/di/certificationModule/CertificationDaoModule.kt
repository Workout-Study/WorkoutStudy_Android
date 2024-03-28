package com.fitmate.fitmate.di.certificationModule

import com.fitmate.fitmate.data.source.CertificationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CertificationDaoModule {

    @Singleton
    @Provides
    fun providesContentDao(database: CertificationDatabase) = database.contentDao()
}