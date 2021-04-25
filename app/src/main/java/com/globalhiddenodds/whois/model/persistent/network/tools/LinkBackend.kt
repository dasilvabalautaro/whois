package com.globalhiddenodds.whois.model.persistent.network.tools

import com.globalhiddenodds.whois.domain.functional.Either
import com.globalhiddenodds.whois.model.exception.Failure
import retrofit2.Call

object LinkBackend {
    inline fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T):
            Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> Either.Right(transform((response.body() ?: default)))
                false -> Either.Left(Failure.ServerError())
            }
        } catch (exception: Throwable) {
            Either.Left(Failure.ServerError())
        }
    }
}