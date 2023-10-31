package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.communications.NewStatApi
import com.example.entshptapplication.models.StatNaryad
import com.example.entshptapplication.models.StatSummary
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StatViewModel(val newStatApi: NewStatApi): ViewModel() {

    val summary = MutableLiveData<StatSummary>()
    val upakNaryadList = MutableLiveData<List<StatNaryad>>(listOf())
    val shptNaryadList = MutableLiveData<List<StatNaryad>>(listOf())

    val error = MutableLiveData<String?>()

    private val workerId = MutableLiveData<Int?>(null)
    private val selectDate = MutableLiveData<String?>(null)
    private val shptStart = MutableLiveData<Int>(0);
    private var upakStart = MutableLiveData<Int>(0);
    fun getSummary(workerId: Int, selectDate: String? = null){
        shptStart.value = 0
        upakStart.value = 0
        this.workerId.value = workerId
        this.selectDate.value=selectDate
        upakNaryadList.value = listOf()
        shptNaryadList.value = listOf()

        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            selectSummary(workerId, selectDate)
        }
    }

    suspend fun selectSummary(workerId: Int, selectDate: String?){
        Log.d("Start get summary","")
        val result = newStatApi.getSummary(workerId, selectDate)
        summary.postValue(result)
        Log.d("Stat shpt count",result.shptCount.toString())
    }

    fun getUpakDetail(){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            val list = mutableListOf<StatNaryad>()
            list.addAll(upakNaryadList.value!!.toList())
            val start = upakStart.value!!
            val newlist = newStatApi.getDetail(workerId.value!!, 7, selectDate.value, start, 50)
            if(newlist.size==0)
                return@launch
            list.addAll(newlist)
            Log.d("detail size", list.size.toString())
            upakNaryadList.postValue(list)
            upakStart.postValue(start + 50)
        }
    }

    fun getShptDetail(){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            val list = mutableListOf<StatNaryad>()
            list.addAll(shptNaryadList.value!!.toList())
            val start = shptStart.value!!
            val newlist = newStatApi.getDetail(workerId.value!!, 8, selectDate.value, start, 50)
            if(newlist.size==0)
                return@launch
            list.addAll(newlist)
            shptNaryadList.postValue(list)
            shptStart.postValue(start + 50)
            /*
            val list = shptNaryadList.value
            val start = shptStart.value!!
            list!!.containsAll(newStatApi.getDetail(workerId.value!!, 8, selectDate.value, start, 50))
            shptNaryadList.postValue(list!!)
             */
        }
    }

    fun deleteUpak(workerId: Int,naryadId: Int){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            newStatApi.deleteUpakNaryad(naryadId, workerId)
            upakNaryadList.postValue(upakNaryadList.value!!.filter { it.naryadId!=naryadId })
            delay(500)
            selectSummary(workerId, selectDate.value)
        }
    }

    fun deleteShpt(workerId: Int, naryadId: Int) {
        viewModelScope.launch(Dispatchers.IO + getCoroutineExceptionHandler()) {
            newStatApi.deleteShptNaryad(naryadId, workerId)
            shptNaryadList.postValue(shptNaryadList.value!!.filter { it.naryadId != naryadId })
            delay(500)
            selectSummary(workerId, selectDate.value)
        }
    }

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            Log.e("Stat error", throwable.message.toString())
            error.postValue(throwable.message.toString())
        }
    }
}

class StatViewModelFactory constructor(private val newStatApi: NewStatApi): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatViewModel(newStatApi) as T
    }
}