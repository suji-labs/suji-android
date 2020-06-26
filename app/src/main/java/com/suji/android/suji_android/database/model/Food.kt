package com.suji.android.suji_android.database.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class Food(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "price")
    var price: Int,
    @ColumnInfo(name = "sub")
    var subMenu: ArrayList<Food> = ArrayList<Food>(),
    @ColumnInfo(name = "count")
    var count: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        ArrayList<Food>(),
        parcel.readInt(),
        parcel.readInt()
    ) {
        parcel.readTypedList(subMenu, Food.CREATOR)
    }

    constructor() : this("", 0, ArrayList<Food>(), 0)

    override fun equals(other: Any?): Boolean {
        if (other is Food) {
            return other.name == this.name && other.id == this.id
        }

        return false
    }

    override fun hashCode(): Int {
//        return super.hashCode()
        return id
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeInt(price)
        dest.writeTypedList(subMenu)
        dest.writeInt(count)
        dest.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }
}