package com.sakharu.queregardercesoir.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun fromString(value: String?): List<Long> {
        val listType: Type = object : TypeToken<ArrayList<Long?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<Long>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}