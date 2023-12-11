package com.example.entshptapplication.ui.naryadInfo

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.ui.naryadInfo.Models.NaryadInfoModel
import com.example.entshptapplication.ui.naryadInfo.api.NaryadInfoApi
import com.example.entshptapplication.models.HOSTED_NAME
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class NaryadInfoViewModel constructor(private val findApi: NaryadInfoApi): ViewModel() {
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

class NaryadInfoViewModelFactory constructor(private val findApi: NaryadInfoApi): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NaryadInfoViewModel(this.findApi) as T
    }

    companion object{
        fun Create(fragment: Fragment): NaryadInfoViewModel {
            val findApi = NaryadInfoApi.getInstance(HOSTED_NAME)
            return ViewModelProvider(fragment.requireActivity().viewModelStore, NaryadInfoViewModelFactory(findApi)).get(
                NaryadInfoViewModel::class.java)
        }
    }
}