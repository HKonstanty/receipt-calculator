package com.example.receiptapp.userData

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes

data class User(
    val id: Long,
    val name: String?,
    val number: String,
    val email: String,
    @DrawableRes
    val image: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString()!!,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(number)
        parcel.writeString(email)
        parcel.writeValue(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}