package com.fitmate.fitmate.data.source

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }
}