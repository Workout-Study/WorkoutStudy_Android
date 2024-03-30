package com.fitmate.fitmate.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.data.source.dao.ChatDao

@Database(entities = [ChatEntity::class], version = 1)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun contentDao(): ChatDao
}