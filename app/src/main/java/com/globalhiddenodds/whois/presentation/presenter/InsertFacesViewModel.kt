package com.globalhiddenodds.whois.presentation.presenter

import androidx.lifecycle.MutableLiveData
import com.globalhiddenodds.whois.domain.data.Face
import com.globalhiddenodds.whois.domain.interactor.InsertFacesUseCase
import com.globalhiddenodds.whois.presentation.plataform.BaseViewModel
import javax.inject.Inject

class InsertFacesViewModel @Inject constructor(
    private val insertFacesUseCase: InsertFacesUseCase):
    BaseViewModel() {
    var result: MutableLiveData<Boolean> = MutableLiveData()

    var list: List<Face>? = null

    fun insertFaces() = insertFacesUseCase(list!!){
        it.either(::handleFailure, ::handleResult)
    }

    private fun handleResult(value: Boolean){
        result.value = value

    }
}