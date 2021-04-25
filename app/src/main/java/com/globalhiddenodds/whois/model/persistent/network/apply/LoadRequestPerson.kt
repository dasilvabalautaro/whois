package com.globalhiddenodds.whois.model.persistent.network.apply

import com.globalhiddenodds.whois.domain.data.Person
import com.globalhiddenodds.whois.domain.functional.Either
import com.globalhiddenodds.whois.model.exception.Failure
import com.globalhiddenodds.whois.model.persistent.network.entity.PersonEntity
import com.globalhiddenodds.whois.model.persistent.network.services.BodyRequestPerson
import com.globalhiddenodds.whois.model.persistent.network.tools.LinkBackend
import com.globalhiddenodds.whois.presentation.plataform.NetworkHandler
import okhttp3.RequestBody
import javax.inject.Inject

interface LoadRequestPerson {
    fun getResult(url: String, body: RequestBody):
            Either<Failure, Person>
    class SendRequest @Inject constructor(private val networkHandler: NetworkHandler,
                                          private val bodyRequest: BodyRequestPerson
    ):
        LoadRequestPerson{
        override fun getResult(url: String, body: RequestBody): Either<Failure, Person> {
            return when (networkHandler.isConnected) {
                true -> LinkBackend.request(bodyRequest.send(url, body),
                    { it.toPerson() },
                    PersonEntity(0, "", "")
                )
                false -> Either.Left(Failure.NetworkConnection())
            }
        }

    }
}