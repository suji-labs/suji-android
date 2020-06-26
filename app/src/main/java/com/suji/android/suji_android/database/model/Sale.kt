package com.suji.android.suji_android.database.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.suji.android.suji_android.helper.Constant

@Entity(tableName = "sale")
data class Sale(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "totalPrice")
    var totalPrice: Int,
    @ColumnInfo(name = "salesTime")
    var salesTime: Long,
    @ColumnInfo(name = "orderedFoods")
    var orderedFoods: ArrayList<Food> = ArrayList<Food>(),
    @ColumnInfo(name = "isSale")
    var isSale: Boolean = false,
    @ColumnInfo(name = "pay")
    var pay: Int = Constant.PayType.NOT_PAY,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readLong(),
        ArrayList<Food>(),
    parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt()
    ) {
        parcel.readTypedList(orderedFoods, Food.CREATOR)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeInt(totalPrice)
        dest.writeLong(salesTime)
        dest.writeTypedList(orderedFoods)
        dest.writeByte(if (isSale) 1 else 0)
        dest.writeInt(pay)
        dest.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sale> {
        override fun createFromParcel(parcel: Parcel): Sale {
            return Sale(parcel)
        }

        override fun newArray(size: Int): Array<Sale?> {
            return arrayOfNulls(size)
        }
    }
}