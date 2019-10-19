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
    val id: Int = 0
) {
    constructor() : this("", 0, ArrayList<Food>(), 0)

    override fun equals(other: Any?): Boolean {
        if (other is Food) {
            return other.name == this.name
        }

        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}