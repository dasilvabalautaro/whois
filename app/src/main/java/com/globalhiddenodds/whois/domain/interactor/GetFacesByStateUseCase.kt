package com.globalhiddenodds.whois.domain.interactor

import com.globalhiddenodds.whois.domain.data.Face
import com.globalhiddenodds.whois.domain.functional.Either
import com.globalhiddenodds.whois.model.database.interfaces.FaceDataDao
import com.globalhiddenodds.whois.model.exception.Failure
import javax.inject.Inject

class GetFacesByStateUseCase @Inject constructor(
    private val faceDataDao: FaceDataDao): UseCase<List<Face>, Int>() {

    override suspend fun run(params: Int): Either<Failure, List<Face>> {
        return try {
            val listData = faceDataDao.findFacesByState(params)

            val list = listData.map { Face(it.id, it.name,
                it.document, it.base64, it.latitude,
                it.longitude, it.date, it.state) }

            when(list.isNotEmpty()){
                true -> Either.Right(list)
                false -> Either.Left(Failure.DatabaseError())
            }


        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }
    }
}