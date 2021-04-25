package com.globalhiddenodds.whois.domain.interactor

import com.globalhiddenodds.whois.domain.data.Face
import com.globalhiddenodds.whois.domain.functional.Either
import com.globalhiddenodds.whois.model.database.data.FaceData
import com.globalhiddenodds.whois.model.database.interfaces.FaceDataDao
import com.globalhiddenodds.whois.model.exception.Failure
import javax.inject.Inject

class InsertFacesUseCase @Inject constructor(private val faceDataDao: FaceDataDao): UseCase<Boolean, List<Face>>() {

    override suspend fun run(params: List<Face>): Either<Failure, Boolean> {
        return try {
            val list = transformList(params)

            when (list.isNotEmpty()){
                true -> {
                    faceDataDao.insertFaces(list)
                    Either.Right(true)
                }
                false -> Either.Left(Failure.DatabaseError())
            }

        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }

    }

    private fun transformList(list: List<Face>): List<FaceData>{
        val faces = ArrayList<FaceData>()

        if (list.isNotEmpty()){
            for (i in list.indices){
                val faceTemp = FaceData(list[i].id, list[i].name,
                    list[i].document, list[i].base64, list[i].latitude,
                    list[i].longitude, list[i].date, list[i].state)
                faces.add(faceTemp)
            }
        }
        return faces.toList()
    }
}