package com.globalhiddenodds.whois.presentation.presenter

import androidx.lifecycle.MutableLiveData
import com.globalhiddenodds.whois.domain.data.Face
import com.globalhiddenodds.whois.domain.interactor.GetFacesByStateUseCase
import com.globalhiddenodds.whois.model.persistent.files.ManageFiles
import com.globalhiddenodds.whois.presentation.data.FaceView
import com.globalhiddenodds.whois.presentation.plataform.BaseViewModel
import javax.inject.Inject

class GetFacesByStateViewModel @Inject constructor(
    private val getFacesByStateUseCase: GetFacesByStateUseCase,
    private val managerFiles: ManageFiles):
    BaseViewModel() {

    var result: MutableLiveData<List<FaceView>> = MutableLiveData()
    var state: Int? = null

    fun loadFaces() = getFacesByStateUseCase(state!!){
        it.either(::handleFailure, ::handlePendingList)
    }

    private fun handlePendingList(faces: List<Face>) {
        this.result.value = faces.map { FaceView(it.id, it.name,
            it.document, managerFiles.base64DecodeImage(it.base64),
                it.date, it.latitude, it.longitude) }
    }
}