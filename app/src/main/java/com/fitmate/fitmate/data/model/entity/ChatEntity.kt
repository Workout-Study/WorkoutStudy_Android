package com.fitmate.fitmate.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity("Chat")
data class ChatEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int ?= null,
    @ColumnInfo
    var fitGroupId: String,
    @ColumnInfo
    var fitmateId: String,
    @ColumnInfo
    var message: String
): Serializable