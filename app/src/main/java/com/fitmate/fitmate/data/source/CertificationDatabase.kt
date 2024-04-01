package com.fitmate.fitmate.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fitmate.fitmate.data.model.entity.CertificationEntity
import com.fitmate.fitmate.data.source.dao.CertificationDao

@Database(entities = [CertificationEntity::class], version = 2)
@TypeConverters(CertificationConverters::class)
abstract class CertificationDatabase: RoomDatabase() {
    abstract fun contentDao(): CertificationDao
}