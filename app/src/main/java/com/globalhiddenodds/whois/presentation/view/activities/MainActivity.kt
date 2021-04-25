package com.globalhiddenodds.whois.presentation.view.activities

import android.os.Bundle
import android.widget.Toast
import com.globalhiddenodds.whois.R
import com.globalhiddenodds.whois.model.exception.NoNetworkException
import com.globalhiddenodds.whois.model.persistent.caching.Variables
import com.globalhiddenodds.whois.presentation.plataform.BaseActivity
import com.globalhiddenodds.whois.presentation.view.fragments.SplashFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class MainActivity : BaseActivity() {

    override fun fragment() = SplashFragment()

    //private lateinit var subscribe: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        checkRESTServer(whoIsWebServices)
        /*subscribe = whoIsWebServices.getPublicEvents()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            Variables.isServerUp = true
            val msg = "name: ${it.name}\n version: ${it.version}\n date: ${it.date}\n connect: ${it.connect}"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }, { throwable ->

            if (throwable is NoNetworkException) {
                Toast.makeText(
                    this,
                    getString(R.string.failure_network_connection),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.msg_error_connection_other),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            Variables.isServerUp = false
        })*/

    }

}