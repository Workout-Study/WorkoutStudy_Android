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
        val value = StringBuilder()
        for (uri in list) {
            value.append(uri.toString())
            value.append(",")
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
}