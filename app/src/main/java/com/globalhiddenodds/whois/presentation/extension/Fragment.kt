//@file:Suppress("DEPRECATION")

package com.globalhiddenodds.whois.presentation.extension

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.globalhiddenodds.whois.presentation.plataform.BaseActivity
import com.globalhiddenodds.whois.presentation.plataform.BaseFragment


inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().commit()

inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
    val vm = ViewModelProvider(this, factory)[T::class.java]
    vm.body()
    return vm
}


fun BaseFragment.close() = (activity as FragmentActivity).supportFragmentManager.popBackStack()

val BaseFragment.viewContainer: View
    get() = (activity as BaseActivity)
        .activityMainBinding.fragmentContainer

val BaseFragment.appContext: Context get() = activity?.applicationContext!!