package com.fitmate.fitmate.di.chatModule

import com.fitmate.fitmate.data.source.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatDaoModule {

    @Singleton
    @Provides
    fun providesContentDao(database: ChatDatabase) = database.contentDao()
}