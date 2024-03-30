package com.fitmate.fitmate.di.chatModule

import android.content.Context
import androidx.room.Room
import com.fitmate.fitmate.data.source.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatDatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): ChatDatabase {
        return Room.databaseBuilder(context, ChatDatabase::class.java, "chat.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}