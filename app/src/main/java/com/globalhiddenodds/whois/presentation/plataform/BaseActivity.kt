package com.globalhiddenodds.whois.presentation.plataform

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.globalhiddenodds.whois.App
import com.globalhiddenodds.whois.R
import com.globalhiddenodds.whois.R.id
import com.globalhiddenodds.whois.databinding.ActivityMainBinding
import com.globalhiddenodds.whois.databinding.ToolbarBinding
import com.globalhiddenodds.whois.di.ApplicationComponent
import com.globalhiddenodds.whois.model.exception.NoNetworkException
import com.globalhiddenodds.whois.model.persistent.caching.Variables
import com.globalhiddenodds.whois.model.persistent.network.interfaces.WhoIsWebServices
import com.globalhiddenodds.whois.presentation.extension.inTransaction
import com.globalhiddenodds.whois.presentation.permission.EnablePermissions
import com.globalhiddenodds.whois.presentation.view.activities.MenuActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.toast
import javax.inject.Inject
import kotlin.system.exitProcess

abstract class BaseActivity: AppCompatActivity() {
    val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    @Inject
    lateinit var whoIsWebServices: WhoIsWebServices
    @Inject
    lateinit var enablePermissions: EnablePermissions

    internal var disposable: CompositeDisposable = CompositeDisposable()
    private var doubleBackToExitPressedOnce = false

    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var bindingToolbar: ToolbarBinding
    lateinit var subscribe: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        bindingToolbar = ToolbarBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)
        addFragment(savedInstanceState)
    }

    private fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction { add(
            id.fragmentContainer, fragment()) }

    abstract fun fragment(): BaseFragment

    @SuppressLint("PrivateResource")
    fun addFragment(newFragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out)
            .replace(id.fragmentContainer, newFragment, newFragment.javaClass.simpleName)
            .commit()

    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
            id.fragmentContainer) as BaseFragment).onBackPressed()

        if (this is MenuActivity){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                (appComponent.context() as App).onTerminate()
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(1)
            }

            this.doubleBackToExitPressedOnce = true
            toast("Please click BACK again to exit")

            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false },
                2000)

        } else{
            super.onBackPressed()
        }
    }

    fun checkRESTServer(whoIsWebServices: WhoIsWebServices){
        subscribe = whoIsWebServices.getPublicEvents()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Variables.isServerUp = true
                val msg = "name: ${it.name}\n version: ${it.version}\n date: ${it.date}\n connect: ${it.connect}"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }, { throwable ->

                if (throwable is NoNetworkException) {
                    Toast.makeText(this, getString(R.string.failure_network_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, getString(R.string.msg_error_connection_other),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Variables.isServerUp = false
            })

    }
}