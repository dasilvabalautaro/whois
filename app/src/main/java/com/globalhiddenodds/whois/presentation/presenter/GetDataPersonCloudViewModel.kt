package com.globalhiddenodds.whois.presentation.presenter

import android.webkit.URLUtil
import androidx.lifecycle.MutableLiveData
import com.globalhiddenodds.whois.domain.data.Person
import com.globalhiddenodds.whois.domain.interactor.GetDataPersonCloudUseCase
import com.globalhiddenodds.whois.presentation.plataform.BaseViewModel
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class GetDataPersonCloudViewModel @Inject constructor(
    private val getDataPersonCloudUseCase: GetDataPersonCloudUseCase):
    BaseViewModel() {
    var result: MutableLiveData<Person> = MutableLiveData()

    lateinit var url: String
    var id = 0
    lateinit var image: String
    private lateinit var params: List<String>

    fun verifyInput(): Boolean{
        if (URLUtil.isValidUrl(this.url) &&
            this.id != 0 &&
            this.image.isNotEmpty()){
            val jsonString = jsonToString()
            if (jsonString.isNotEmpty()){
                this.params = listOf(this.url, jsonString)
                return true
            }

        }
        return false
    }

    private fun jsonToString(): String{
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id", this.id)
            jsonObject.put("image", this.image)
            return jsonObject.toString(2)
        }catch (exception: JSONException){
            println(exception.message)
        }
        return ""
    }

    fun requestDataPerson() = getDataPersonCloudUseCase(this.params){
        it.either(::handleFailure, ::handleResult)
    }

    private fun handleResult(person: Person){

        result.value = person

    }
}