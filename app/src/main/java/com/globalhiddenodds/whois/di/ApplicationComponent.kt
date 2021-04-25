package com.globalhiddenodds.whois.di

import android.content.Context
import com.globalhiddenodds.whois.App
import com.globalhiddenodds.whois.di.viewmodel.ViewModelModule
import com.globalhiddenodds.whois.model.persistent.network.LocationListener
import com.globalhiddenodds.whois.presentation.view.activities.FaceVideoActivity
import com.globalhiddenodds.whois.presentation.view.activities.MainActivity
import com.globalhiddenodds.whois.presentation.view.activities.MenuActivity
import com.globalhiddenodds.whois.presentation.view.fragments.CapturedFragment
import com.globalhiddenodds.whois.presentation.view.fragments.SplashFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(app: App)
    fun context(): Context
    fun inject(mainActivity: MainActivity)
    fun inject(faceVideoActivity: FaceVideoActivity)
    fun inject(menuActivity: MenuActivity)
    fun inject(splashFragment: SplashFragment)
    fun inject(capturedFragment: CapturedFragment)
    fun inject(locationListener: LocationListener)
}