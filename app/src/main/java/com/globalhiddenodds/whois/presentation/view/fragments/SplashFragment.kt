package com.globalhiddenodds.whois.presentation.view.fragments

import android.os.Bundle
import android.os.Handler
import com.globalhiddenodds.whois.R
import com.globalhiddenodds.whois.presentation.navigation.Navigator
import com.globalhiddenodds.whois.presentation.plataform.BaseFragment
import com.globalhiddenodds.whois.presentation.view.activities.MainActivity
import javax.inject.Inject

class SplashFragment: BaseFragment() {
    @Inject
    lateinit var navigator: Navigator

    override fun layoutId() = R.layout.view_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        Handler().postDelayed({
            navigator.showMenu(requireActivity())
            (requireActivity() as MainActivity).finish()
        }, 3000)
    }

    override fun renderFailure(message: Int) {
        hideProgress()
        notify(message)
    }
}