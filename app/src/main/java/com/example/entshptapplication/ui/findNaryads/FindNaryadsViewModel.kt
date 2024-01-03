package com.example.entshptapplication.ui.findNaryads

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.communications.FindNaryadsApi
import com.example.entshptapplication.models.FindNaryadModel
import com.example.entshptapplication.models.Naryad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FindNaryadsViewModel @Inject constructor(
    private val findNaryadsApi: FindNaryadsApi
): ViewModel() {
    val findNaryadList = MutableLiveData<List<FindNaryadModel>>(listOf())
    val naryad = MutableLiveData<FindNaryadModel?>(null)

    fun clear() {
        findNaryadList.value = listOf()
    }

    fun find(findStr:String){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            val naryads = findNaryadsApi.Find(findStr)
            findNaryadList.postValue(naryads)
        }
    }

    fun get(naryadId: Int){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()) {
            var resp = findNaryadsApi.GetNaryad(naryadId)
            naryad.postValue(resp)
        }
    }


    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            Log.e("find naryad error", throwable.message.toString())
        }
    }
}