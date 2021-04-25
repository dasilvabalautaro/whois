package com.globalhiddenodds.whois.model.persistent.network.entity

import android.os.Parcel
import com.globalhiddenodds.whois.presentation.plataform.KParcelable
import com.globalhiddenodds.whois.presentation.plataform.parcelableCreator

data class TestEntity(var name: String?,
                      var version: String?,
                      var date: Long,
                      var connect: String?): KParcelable{
    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::TestEntity)
    }
    constructor(parcel: Parcel) : this(parcel.readString()!!,
        parcel.readString()!!, parcel.readLong(), parcel.readString()!!)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(name)
            writeString(version)
            writeLong(date)
            writeString(connect)

        }
    }
}