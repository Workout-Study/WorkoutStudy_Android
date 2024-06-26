package com.fitmate.fitmate.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime

@Entity("Chat")
data class ChatEntity (
    @PrimaryKey(autoGenerate = false)
    var messageId: String,
    @ColumnInfo
    var fitGroupId: Int,
    @ColumnInfo
    var fitMateId: Int,
    @ColumnInfo
    var message: String,
    @ColumnInfo
    var messageTime: LocalDateTime,
    @ColumnInfo
    var messageType: String
): Serializable