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
//    : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString() ?: "",
//        parcel.readInt(),
//        parcel.readLong(),
//        parcel.readTypedObject(HashSet<Food>),
//        parcel.readByte() != 0.toByte(),
//        parcel.readInt(),
//        parcel.readInt()
//    ) {
//    }
//
//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        dest.writeString(name)
//        dest.writeInt(price)
//        dest.writeLong(time)
//        dest.writeTypedList(foods.toList())
//        dest.writeByte(if (isSale) 1 else 0)
//        dest.writeInt(pay)
//        dest.writeInt(id)
//    }
//
//    override fun describeContents(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    companion object CREATOR : Parcelable.Creator<Sale> {
//        override fun createFromParcel(parcel: Parcel): Sale {
//            return Sale(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Sale?> {
//            return arrayOfNulls(size)
//        }
//    }
//}