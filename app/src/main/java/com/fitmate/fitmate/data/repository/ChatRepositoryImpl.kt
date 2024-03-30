package com.fitmate.fitmate.data.repository

import android.util.Log
import com.fitmate.fitmate.data.model.ChatMapper.toDBChat
import com.fitmate.fitmate.data.model.ChatMapper.toEntity
import com.fitmate.fitmate.data.source.ChatDatabase
import com.fitmate.fitmate.data.source.dao.ChatDao
import com.fitmate.fitmate.domain.model.ChatItem
import com.fitmate.fitmate.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao, override val chatDatabase: ChatDatabase
): ChatRepository {
    override fun load(): Flow<List<ChatItem>> {
        return flow{
            chatDao.selectAll().collect(){list->
                emit(list.map {it.toDBChat() })
            }
        }
    }

    override suspend fun insert(item: ChatItem): Boolean {
        return try {
            chatDao.insert(item.toEntity())
            performCheckpoint()
            true
        } catch (e: Exception) {
            Log.d("woojugoing", "Insert Problem : ${e.message}")
            false
        }
    }

    private fun performCheckpoint() {
        try {
            // RoomDatabase의 직접적인 SQL 실행을 위한 접근
            val db = chatDatabase.openHelper.writableDatabase
            db.execSQL("PRAGMA wal_checkpoint(FULL)")
        } catch (e: Exception) {
            Log.d("woojugoing", "Checkpoint failed: ${e.message}")
        }
    }

    override suspend fun update(item: ChatItem): Boolean {
        return try {
            chatDao.update(item.toEntity())
            true
        } catch (e: Exception) {
            Log.d("room_chat", "${e.message}")
            false
        }
    }

}