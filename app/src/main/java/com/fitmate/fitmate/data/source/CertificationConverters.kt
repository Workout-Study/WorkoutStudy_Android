package com.fitmate.fitmate.data.source

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.net.URI
import java.time.Instant

class CertificationConverters {

    @TypeConverter
    fun listToJson(list: MutableList<Uri>): String {
        if (list.isEmpty()) {
            return ""
        }
        val value = StringBuilder()
        for ((index, uri) in list.withIndex()) {
            value.append(uri.toString())
            if (index < list.size - 1) {
                value.append(",")
            }
        }
        return value.toString()
    }
    @TypeConverter
    fun jsonToList(value: String): MutableList<Uri> {
        val listType = mutableListOf<Uri>()
        val array = value.split(",").toTypedArray()
        for (item in array) {
            listType.add(Uri.parse(item))
        }
        return listType
    }

    @TypeConverter
    fun fromInstantToString(instant: Instant?): String? {
        return instant?.toString()
    }

    @TypeConverter
    fun fromStringToInstant(string: String?): Instant? {
        return string?.let { Instant.parse(it) }
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(string: String?): List<String>? {
        return string?.split(",")?.map { it.trim() }
    }
}