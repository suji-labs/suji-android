package com.suji.android.suji_android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class Food(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name = "sub")
    val sub: ArrayList<Food> = ArrayList<Food>(),
    @ColumnInfo(name = "count")
    var count: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)