package com.fitmate.fitmate.data.source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import androidx.room.Update
import com.fitmate.fitmate.data.model.entity.ChatEntity

@Dao
interface ChatDao {

    @Query("SELECT * FROM Chat")
    fun selectAll(): Flow<List<ChatEntity>>

    @Insert
    suspend fun insert(item: ChatEntity)

    @Update
    suspend fun update(item: ChatEntity)
}