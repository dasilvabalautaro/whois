package com.globalhiddenodds.whois.domain.interactor

import com.globalhiddenodds.whois.domain.data.Person
import com.globalhiddenodds.whois.domain.functional.Either
import com.globalhiddenodds.whois.model.exception.Failure
import com.globalhiddenodds.whois.model.persistent.network.apply.LoadRequestPerson
import com.globalhiddenodds.whois.model.persistent.network.tools.Conversion
import javax.inject.Inject

class GetDataPersonCloudUseCase @Inject constructor(
    private val loadRequestPerson: LoadRequestPerson):
    UseCase<Person, List<String>>() {
    override suspend fun run(params: List<String>): Either<Failure, Person> {

        val url = params[0]
        val body = Conversion.createRequestBody(params[1])
        return loadRequestPerson.getResult(url, body)
    }
}
