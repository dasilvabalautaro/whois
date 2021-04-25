package com.globalhiddenodds.whois.model.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faceData")
data class FaceData(@PrimaryKey(autoGenerate = true) var id: Int,
                    @ColumnInfo(name = "name") var name: String,
                    @ColumnInfo(name = "document") var document: String,
                    @ColumnInfo(name = "base64") var base64: String,
                    @ColumnInfo(name = "latitude") var latitude: Double,
                    @ColumnInfo(name = "longitude") var longitude: Double,
                    @ColumnInfo(name = "date") var date: Long,
                    @ColumnInfo(name = "state") var state: Int)