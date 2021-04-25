package com.globalhiddenodds.whois.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globalhiddenodds.whois.model.database.data.FaceData
import com.globalhiddenodds.whois.model.database.interfaces.FaceDataDao

@Database(entities = [FaceData::class],
    version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun faceDataDao(): FaceDataDao
}