package com.globalhiddenodds.whois.model.persistent.network.interfaces

import com.globalhiddenodds.whois.model.persistent.network.entity.TestEntity
import io.reactivex.Observable
import retrofit2.http.GET

interface WhoIsWebServices {
    @GET("test")
    fun getPublicEvents(): Observable<TestEntity>
}