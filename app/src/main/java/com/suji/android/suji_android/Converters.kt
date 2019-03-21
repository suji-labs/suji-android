package com.suji.android.suji_android

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.suji.android.suji_android.model.Food

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
}

/*
@TypeConverter
public static ArrayList<String> fromString(String value) {
    Type listType = new TypeToken<ArrayList<String>>() {}.getType();
    return new Gson().fromJson(value, listType);
}

@TypeConverter
public static String fromArrayList(ArrayList<String> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
}
 */