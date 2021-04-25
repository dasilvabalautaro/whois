package com.globalhiddenodds.whois.model.persistent.network.services

import com.globalhiddenodds.whois.model.persistent.network.interfaces.SkeletonRequestPerson
import okhttp3.RequestBody
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BodyRequestPerson @Inject constructor(retrofit: Retrofit):
    SkeletonRequestPerson{
    private val skeletonRequest by lazy {
        retrofit.create(SkeletonRequestPerson::class.java) }

    override fun send(url: String, body: RequestBody) = skeletonRequest.send(url, body)

}