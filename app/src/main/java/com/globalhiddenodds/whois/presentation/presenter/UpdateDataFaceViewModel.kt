package com.globalhiddenodds.whois.presentation.presenter

import androidx.lifecycle.MutableLiveData
import com.globalhiddenodds.whois.domain.interactor.UpdateDataFaceUseCase
import com.globalhiddenodds.whois.presentation.plataform.BaseViewModel
import javax.inject.Inject

class UpdateDataFaceViewModel @Inject constructor(
    private val updateDataFaceUseCase: UpdateDataFaceUseCase):
    BaseViewModel() {
    var result: MutableLiveData<Boolean> = MutableLiveData()

    var id: Int? = null;
    lateinit var name: String
    lateinit var document: String

    private lateinit var params: MutableList<Any>

    fun buildParams(){
        this.params = mutableListOf<Any>()
        this.params.add(id!!)
        this.params.add(name)
        this.params.add(document)
    }

    fun updateDataFace() = updateDataFaceUseCase(params){
        it.either(::handleFailure, ::handleResult)
    }

    private fun handleResult(value: Boolean){
        result.value = value
    }

}