package com.suji.android.suji_android.database

import android.os.Build
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.helper.Log
import com.suji.android.suji_android.helper.Utils
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset

class Converters {
    @TypeConverter
    fun fromMapString(value: String): HashSet<Food> {
        val listType = object : TypeToken<HashSet<Food>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromMap(list: HashSet<Food>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

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

//    @TypeConverter
//    fun toDate(timestamp: Long?): LocalDateTime? {
//        return if (timestamp == null) {
//            null
//        } else {
//            Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
//        }
//    }

    @TypeConverter
    fun fromTimestamp(timestamp: Long?): LocalDateTime? {
        return Instant.ofEpochMilli(timestamp!!).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    @TypeConverter
    fun toTimestamp(date: LocalDateTime?): Long? {
        return date?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
    }
}