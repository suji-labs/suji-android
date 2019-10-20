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
}
//    : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString() ?: "",
//        parcel.readInt(),
//        parcel.createTypedArrayList(Food),
//        parcel.readInt(),
//        parcel.readInt()
//    ) {
//    }
//
//    constructor() : this("", 0, ArrayList<Food>(), 0)
//
//    override fun equals(other: Any?): Boolean {
//        if (other is Food) {
//            return other.name == this.name
//        }
//
//        return false
//    }
//
//    override fun hashCode(): Int {
//        return super.hashCode()
//    }
//
//    override fun writeToParcel(dest: Parcel?, flags: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun describeContents(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    companion object CREATOR : Parcelable.Creator<Food> {
//        override fun createFromParcel(parcel: Parcel): Food {
//            return Food(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Food?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//
//}