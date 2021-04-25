package com.globalhiddenodds.whois.presentation.data

import android.graphics.Bitmap
import android.os.Parcel
import com.globalhiddenodds.whois.presentation.plataform.KParcelable
import com.globalhiddenodds.whois.presentation.plataform.parcelableCreator

data class FaceView(var id: Int,
                    var name: String?,
                    var document: String?,
                    var image: Bitmap,
                    var date: Long,
                    var latitude: Double,
                    var longitude: Double): KParcelable {
    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::FaceView)
    }

    constructor(parcel: Parcel) : this(parcel.readInt(),
        parcel.readString()!!, parcel.readString()!!,
            parcel.readParcelable<Bitmap>(Bitmap::class.java.classLoader) as Bitmap,
            parcel.readLong(), parcel.readDouble(), parcel.readDouble())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(name)
            writeString(document)
            writeValue(image)
            writeLong(date)
            writeDouble(latitude)
            writeDouble(longitude)

        }
    }
}