package com.androminor.mangaapp.data.local.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Varun Singh
 */
class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val ListType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, ListType)
    }
}