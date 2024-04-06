package com.fitmate.fitmate.data.source

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
    }
}