package com.globalhiddenodds.whois.presentation.plataform

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.globalhiddenodds.whois.App
import com.globalhiddenodds.whois.R
import com.globalhiddenodds.whois.di.ApplicationComponent
import com.globalhiddenodds.whois.model.exception.Failure
import com.globalhiddenodds.whois.presentation.extension.viewContainer
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment: Fragment() {


    abstract fun layoutId(): Int
    internal var disposable: CompositeDisposable = CompositeDisposable()
    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as App).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId(), container, false)

    open fun onBackPressed() {}

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun showProgress() = progressStatus(View.VISIBLE)

    internal fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) =
        with(activity) { if (this is BaseActivity)
            this.bindingToolbar.progress.visibility = viewStatus }

    abstract fun renderFailure(@StringRes message: Int)

    internal fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is Failure.DatabaseError -> renderFailure(R.string.failure_database_error)
            else -> renderFailure(R.string.failure_not_found)
        }
    }

    internal fun notify(@StringRes message: Int) =
        Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()

    internal fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}