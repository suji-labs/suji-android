package com.suji.android.suji_android.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.suji.android.suji_android.database.model.Food
import org.joda.time.DateTime

class Converters {
    @TypeConverter
    fun fromString(value: String): ArrayList<Food> {
        val listType = object : TypeToken<ArrayList<Food>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Food>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toDate(timestamp: Long?): DateTime? {
        return if (timestamp == null) null else DateTime(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: DateTime?): Long? {
        return date?.millis
    }
}