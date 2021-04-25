package com.globalhiddenodds.whois.model.database.interfaces

import androidx.room.*
import com.globalhiddenodds.whois.model.database.data.FaceData

@Dao
interface FaceDataDao {
    @Query("SELECT * FROM faceData")
    fun getAll(): List<FaceData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(faceData: FaceData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFaces(faces: List<FaceData>)

    @Query("DELETE FROM faceData")
    fun deleteAll()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(faceData: FaceData)

    @Query("SELECT * FROM faceData WHERE id LIKE :id")
    fun findFaceById(id: Int): FaceData

    @Query("SELECT * FROM faceData WHERE state LIKE :state")
    fun findFacesByState(state: Int): List<FaceData>

    @Query("UPDATE faceData SET name = :name, document = :document, state = 1 WHERE id = :id")
    fun updateData(id: Int, name: String, document: String)
}