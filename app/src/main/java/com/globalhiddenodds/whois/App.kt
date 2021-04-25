package com.globalhiddenodds.whois

import android.app.Application
import com.globalhiddenodds.whois.di.ApplicationComponent
import com.globalhiddenodds.whois.di.ApplicationModule
import com.globalhiddenodds.whois.di.DaggerApplicationComponent

class App: Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
    }

    override fun onTerminate() {
        super.onTerminate()

        //Realm.getDefaultInstance().close()
        //val config: RealmConfiguration = Realm.getDefaultConfiguration()!!
        //println(config.path + " " + Realm.getGlobalInstanceCount(config).toString())
        println("Terminate")
    }
    private fun injectMembers() = appComponent.inject(this)

}