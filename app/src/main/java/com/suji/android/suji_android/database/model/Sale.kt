package com.suji.android.suji_android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "sale")
data class Sale(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "price")
    var price: Int,
    @ColumnInfo(name = "time")
    var time: DateTime,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)