package com.globalhiddenodds.whois.presentation.plataform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globalhiddenodds.whois.model.exception.Failure

abstract class BaseViewModel: ViewModel() {
    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}