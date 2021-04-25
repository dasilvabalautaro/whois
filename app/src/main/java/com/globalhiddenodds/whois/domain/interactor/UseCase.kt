package com.globalhiddenodds.whois.domain.interactor

import com.globalhiddenodds.whois.domain.functional.Either
import com.globalhiddenodds.whois.model.exception.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCase<out Type, in Params> where Type : Any {
    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params,
                        onResult: (Either<Failure, Type>) -> Unit = {}){

        val job = GlobalScope.async { run(params) }
        GlobalScope.launch(Dispatchers.Main)  { onResult(job.await()) }
    }


    class None
}