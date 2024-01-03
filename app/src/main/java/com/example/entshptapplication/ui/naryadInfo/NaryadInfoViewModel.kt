package com.example.entshptapplication.ui.naryadInfo

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.ui.naryadInfo.Models.NaryadInfoModel
import com.example.entshptapplication.ui.naryadInfo.api.NaryadInfoApi
import com.example.entshptapplication.models.HOSTED_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NaryadInfoViewModel @Inject constructor(
    private val findApi: NaryadInfoApi
): ViewModel() {
    val findNaryad = MutableLiveData<NaryadInfoModel?>(null)

    fun clear(){
        findNaryad.value=null;
    }
    fun getByNaryadNum(findText: String){
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            val naryad = findApi.Get(findText)
            findNaryad.postValue(naryad)
        }
    }

    fun getByNaryadId(barcode: String){
        val naryadId: String = barcode.lowercase().replace("n","").replace("e","")
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            val naryad = findApi.GetById(naryadId)
            findNaryad.postValue(naryad)
        }
    }

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            //error.postValue(throwable.message.toString())
        }
    }
}