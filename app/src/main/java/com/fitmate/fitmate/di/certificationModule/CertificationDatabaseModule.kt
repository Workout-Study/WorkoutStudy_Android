package com.fitmate.fitmate.di.certificationModule

import android.content.Context
import androidx.room.Room
import com.fitmate.fitmate.data.source.CertificationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CertificationDatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): CertificationDatabase {
        return Room.databaseBuilder(context, CertificationDatabase::class.java, "blindappproject.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}