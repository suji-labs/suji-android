package com.suji.android.suji_android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.suji.android.suji_android.helper.Constant

@Entity(tableName = "sale")
data class Sale(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "price")
    var price: Int,
    @ColumnInfo(name = "time")
    var time: Long,
    @ColumnInfo(name = "foods")
    var foods: HashSet<Food> = HashSet<Food>(),
    @ColumnInfo(name = "isSale")
    var isSale: Boolean = false,
    @ColumnInfo(name = "pay")
    var pay: Int = Constant.PayType.NOT_PAY,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)