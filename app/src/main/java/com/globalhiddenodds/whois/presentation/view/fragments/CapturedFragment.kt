package com.globalhiddenodds.whois.presentation.view.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.globalhiddenodds.whois.R
import com.globalhiddenodds.whois.databinding.ViewCapturedFacesBinding
import com.globalhiddenodds.whois.domain.data.Face
import com.globalhiddenodds.whois.domain.data.Person
import com.globalhiddenodds.whois.model.persistent.caching.Constants
import com.globalhiddenodds.whois.model.persistent.caching.Variables
import com.globalhiddenodds.whois.model.persistent.files.ManageFiles
import com.globalhiddenodds.whois.presentation.component.FaceAdapter
import com.globalhiddenodds.whois.presentation.data.DataTemp
import com.globalhiddenodds.whois.presentation.data.FaceView
import com.globalhiddenodds.whois.presentation.extension.*
import com.globalhiddenodds.whois.presentation.permission.EnablePermissions
import com.globalhiddenodds.whois.presentation.plataform.BaseFragment
import com.globalhiddenodds.whois.presentation.presenter.GetDataPersonCloudViewModel
import com.globalhiddenodds.whois.presentation.presenter.GetFacesByStateViewModel
import com.globalhiddenodds.whois.presentation.presenter.InsertFacesViewModel
import com.globalhiddenodds.whois.presentation.presenter.UpdateDataFaceViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CapturedFragment: BaseFragment() {
    companion object{
        fun deleteRecycler(){
            DataTemp.clearListBitmap()
            DataTemp.clearListFaces()
        }
    }

    @Inject
    lateinit var facesAdapter: FaceAdapter
    @Inject
    lateinit var enablePermissions: EnablePermissions
    @Inject
    lateinit var manageFiles: ManageFiles

    private lateinit var insertFacesViewModel: InsertFacesViewModel
    private lateinit var getFacesByStateViewModel: GetFacesByStateViewModel
    private lateinit var getDataPersonCloudViewModel: GetDataPersonCloudViewModel
    private lateinit var updateDataFaceViewModel: UpdateDataFaceViewModel

    private var fragmentFacesBinding: ViewCapturedFacesBinding? = null

    override fun layoutId() = R.layout.view_captured_faces

    private var disposableChangeList: Disposable? = null

    private var countUpdate = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val changeList = DataTemp.observableList.map { s -> s }
        disposableChangeList = changeList.observeOn(AndroidSchedulers.mainThread())
            .subscribe { s ->
                kotlin.run {
                    if(s == "clear"){
                        facesAdapter.collection = emptyList()
                        fragmentFacesBinding!!.tvTitle.text = getString(R.string.lbl_captured)
                        countUpdate = 0
                        fragmentFacesBinding!!.tvNumberFaces.text = countUpdate.toString()

                    }

                }
            }

        val binding = ViewCapturedFacesBinding.bind(view)
        fragmentFacesBinding = binding
        insertFacesViewModel = viewModel(viewModelFactory) {
            observe(result, ::handleResultInsertFaces)
            failure(failure, ::handleFailure)
        }

        getFacesByStateViewModel = viewModel(viewModelFactory) {
            observe(result, ::handleResultListPending)
            failure(failure, ::handleFailure)
        }

        getDataPersonCloudViewModel = viewModel(viewModelFactory) {
            observe(result, ::handleResultDataPerson)
            failure(failure, ::handleFailure)
        }

        updateDataFaceViewModel = viewModel(viewModelFactory) {
            observe(result, ::handleResultUpdateDataFace)
            failure(failure, ::handleFailure)
        }
        fragmentFacesBinding!!.ibAdd.setOnClickListener{insertFacesDatabase()}
        initializeView()
    }

    override fun onResume() {
        super.onResume()

        fragmentFacesBinding!!.tvDate.text = Date().toString()

        if (DataTemp.getListBitmap().isNotEmpty()){
            val list = DataTemp.getListBitmap().toList()
            for (img: Bitmap in list){
                val face = FaceView(0, String.empty(), String.empty(),
                        img, System.currentTimeMillis(),
                    Variables.locationUser.lat, Variables.locationUser.lon)
                DataTemp.addFace(face)
            }
            facesAdapter.collection = DataTemp.getListFaces().toList()
            fragmentFacesBinding!!.tvNumberFaces.text = DataTemp.getListFaces().count().toString()
            fragmentFacesBinding!!.tvTitle.text = getString(R.string.lbl_identify)
        }

    }

    private fun initializeView(){
        fragmentFacesBinding!!.rvPhotos.setHasFixedSize(true)
        fragmentFacesBinding!!.rvPhotos.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL, false)
        addDecorationRecycler(fragmentFacesBinding!!.rvPhotos, requireContext())
        fragmentFacesBinding!!.rvPhotos.adapter = facesAdapter

    }

    private fun handleResultInsertFaces(value: Boolean?){
        if (value != null && value){
            requireContext().toast("Insert OK")
            getFacesByStateViewModel.state = 0
            getFacesByStateViewModel.loadFaces()
        }
    }

    private fun handleResultListPending(list: List<FaceView>?){
        if (!list.isNullOrEmpty() && Variables.isServerUp){
            val url = String.format("${Constants.urlBase}${Constants.serviceRecognition}")
            for (face: FaceView in list){
                getDataPersonCloudViewModel.url = url
                getDataPersonCloudViewModel.id = face.id
                getDataPersonCloudViewModel.image = manageFiles.base641EncodedImage(face.image)
                if (getDataPersonCloudViewModel.verifyInput()) {
                    getDataPersonCloudViewModel.requestDataPerson()
                    DataTemp.addFace(face)

                }
            }
        }
        else{
            requireContext().toast("Network not connected. Identified suspend.")
        }
    }

    private fun handleResultDataPerson(person: Person?){
        if(person != null && person.id != 0){
            updateDataFaceViewModel.id = person.id
            updateDataFaceViewModel.name = person.name
            updateDataFaceViewModel.document = person.document
            updateDataFaceViewModel.buildParams()
            updateDataFaceViewModel.updateDataFace()
            DataTemp.getListFaces().find {
                it.id == person.id }!!.name = person.name
            DataTemp.getListFaces().find {
                it.id == person.id }!!.document = person.document

        }
    }

    private fun handleResultUpdateDataFace(value: Boolean?){
        if(countUpdate == 0) fragmentFacesBinding!!.tvTitle.text = getString(R.string.lbl_identified_faces)
        if (value != null && value){
            countUpdate++
            facesAdapter.collection = DataTemp.getListFaces().toList()
            fragmentFacesBinding!!.tvNumberFaces.text = countUpdate.toString()
        }


    }

    private fun insertFacesDatabase(){
        if (DataTemp.getListFaces().isNotEmpty()){
            val list = ArrayList<Face>()
            val listFaces = DataTemp.getListFaces().toList()
            for (fv: FaceView in listFaces){
                val base64 = manageFiles.base641EncodedImage(fv.image)
                val faceTemp = Face(0, String.empty(), String.empty(),
                base64, Variables.locationUser.lat, Variables.locationUser.lon,
                    System.currentTimeMillis(), 0)
                list.add(faceTemp)
            }
            insertFacesViewModel.list = list.toList()
            insertFacesViewModel.insertFaces()
            DataTemp.clearListFaces()
        }
        else{
            requireContext().toast(getString(R.string.failure_data_not_found))
        }
    }

    override fun renderFailure(message: Int) {
        hideProgress()
        notify(message)
    }

    override fun onDestroyView() {
        fragmentFacesBinding = null
        super.onDestroyView()
    }
}