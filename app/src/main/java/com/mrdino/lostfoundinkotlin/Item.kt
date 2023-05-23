package com.mrdino.lostfoundinkotlin

import android.os.Parcel
import android.os.Parcelable

data class Item(
    val id: Long,
    val type: String,
    val name: String,
    val contact: String,
    val description: String,
    val date: String,
    val location: String,
    val latitude: Double,   // Latitude property
    val longitude: Double   // Longitude property
) : Parcelable {
    override fun describeContents(): Int {
        return 0
}
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        // Write item properties to the parcel
        parcel.writeLong(id)
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeString(contact)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(location)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        // ...
    }

    // Parcelable CREATOR
    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            // Read item properties from the parcel and return a new Item object
            return Item(
                parcel.readLong(),
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readDouble(),
                parcel.readDouble()
                // ...
            )
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}
