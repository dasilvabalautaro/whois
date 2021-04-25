package com.globalhiddenodds.whois.model.persistent.network.interfaces

import com.globalhiddenodds.whois.model.persistent.network.entity.PersonEntity
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

internal interface SkeletonRequestPerson {
    @POST
    fun send(@Url url: String, @Body body: RequestBody): Call<PersonEntity>
}