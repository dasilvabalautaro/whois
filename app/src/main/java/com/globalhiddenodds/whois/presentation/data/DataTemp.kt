package com.globalhiddenodds.whois.presentation.data

import android.graphics.Bitmap
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


object DataTemp {
    var observableList: Subject<String> = PublishSubject.create()
    private var listFaces = ArrayList<FaceView>()
    private var listBitmap = ArrayList<Bitmap>()

    fun clearListFaces(){
        listFaces.clear()
        this.observableList.onNext("clear")
    }

    fun addFace(face: FaceView){
        listFaces.add(face)
    }

    fun getListFaces(): ArrayList<FaceView>{
        return listFaces
    }

    fun clearListBitmap(){
        listBitmap.clear()
    }

    fun addBitmap(bmp: Bitmap){
        listBitmap.add(bmp)
    }

    fun getListBitmap(): ArrayList<Bitmap>{
        return listBitmap
    }
}