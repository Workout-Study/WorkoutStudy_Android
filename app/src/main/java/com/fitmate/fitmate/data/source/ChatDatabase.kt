package com.fitmate.fitmate.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.data.source.dao.ChatDao

@Database(entities = [ChatEntity::class], version = 2)
@TypeConverters(ChatTimeConverter::class)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun contentDao(): ChatDao
}