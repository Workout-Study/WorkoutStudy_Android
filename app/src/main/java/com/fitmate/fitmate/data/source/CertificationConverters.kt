package com.fitmate.fitmate.data.source

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.net.URI
import java.time.Instant

class CertificationConverters {

    @TypeConverter
    fun listToJson(value: MutableList<URI>): String{
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): MutableList<URI>{
        return Gson().fromJson(value, Array<URI>::class.java).toMutableList()
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