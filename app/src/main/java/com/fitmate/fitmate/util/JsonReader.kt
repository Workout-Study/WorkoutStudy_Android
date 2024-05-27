package com.fitmate.fitmate.util

import android.content.Context
import com.google.gson.Gson
import java.io.IOException

fun <T> Context.readData(fileName:String, classT: Class<T>): T? {
    return try {
        val inputStream = this.resources.assets.open(fileName)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)
        inputStream.close()

        val gson = Gson()
        return gson.fromJson(String(byteArray),classT)
    }catch (e: IOException){
        //오류 발생
        null
    }
}