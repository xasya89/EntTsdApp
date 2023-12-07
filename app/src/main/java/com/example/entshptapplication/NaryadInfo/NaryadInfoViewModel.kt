package com.example.entshptapplication.NaryadInfo

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.NaryadInfo.Models.NaryadInfoModel
import com.example.entshptapplication.NaryadInfo.api.NaryadInfoApi
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

    fun getByNaryadCompliteId(barcode: String){
        val naryadCompliteId: Int = barcode.lowercase().replace("n","").replace("e","").toInt()
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            val naryad = findApi.Get(naryadCompliteId)
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
        fun Create(fragment: Fragment): NaryadInfoViewModel{
            val findApi = NaryadInfoApi.getInstance(HOSTED_NAME)
            return ViewModelProvider(fragment.requireActivity().viewModelStore, NaryadInfoViewModelFactory(findApi)).get(NaryadInfoViewModel::class.java)
        }
    }
}