package com.globalhiddenodds.whois.model.persistent.network.tools

import okhttp3.MediaType
import okhttp3.RequestBody

object Conversion {
    fun createRequestBody(body: String): RequestBody {
        val contentType = "application/json; charset=utf-8"

        return RequestBody.create(
            MediaType.parse(contentType),
            (body)
        )

    }
}