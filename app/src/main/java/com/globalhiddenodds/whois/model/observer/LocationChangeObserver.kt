package com.globalhiddenodds.whois.model.observer

import android.location.Location
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationChangeObserver @Inject constructor() {

    var location: Location? = null
    var observableLocation: Subject<Location> = PublishSubject.create()

}