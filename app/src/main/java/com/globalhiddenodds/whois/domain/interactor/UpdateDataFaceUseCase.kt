package com.globalhiddenodds.whois.domain.interactor

import com.globalhiddenodds.whois.domain.functional.Either
import com.globalhiddenodds.whois.model.database.interfaces.FaceDataDao
import com.globalhiddenodds.whois.model.exception.Failure
import javax.inject.Inject

class UpdateDataFaceUseCase @Inject constructor(
    private val faceDataDao: FaceDataDao):
    UseCase<Boolean, MutableList<Any>>() {

    override suspend fun run(params: MutableList<Any>): Either<Failure, Boolean> {
        return try {
            val id = params[0] as? Int ?: 0
            val name = params[1] as? String ?: ""
            val document = params[2] as? String ?: ""

            faceDataDao.updateData(id, name, document)
            Either.Right(true)

        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }

    }
}