package com.example.entshptapplication.ui.statistics

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.communications.StatisticsApi
import com.example.entshptapplication.ui.statistics.models.CreateModel
import com.example.entshptapplication.ui.statistics.models.SummaryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class StatisticsCreatorViewModel @Inject constructor(
    val statisticsApi: StatisticsApi
): ViewModel() {
    val summary = MutableLiveData<SummaryModel?>(null)
    private val uuidJob = MutableLiveData<String>("")
    fun addJob(workerId: Int){
        summary.value = null
        viewModelScope.launch (getCoroutineExceptionHandler() ){
            val uuid = statisticsApi.addJob(CreateModel(workerId)).uuid
            uuidJob.postValue(uuid)
        }
    }
    fun cancelJob(){
        uuidJob.value=""
    }
    fun getResult(){
        val uuid = uuidJob.value
        if(uuid==null || uuid=="") return
        viewModelScope.launch (getCoroutineExceptionHandler()) {
            val response = statisticsApi.getStatistic(uuid)
            if(response.code()==HttpURLConnection.HTTP_OK && response.body()!=null){
                Log.d("Response", "Success")
                summary.postValue(response.body())
            }else
                Log.d("Response", response.body().toString())
            //summary.postValue(result)
        }
    }

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable -> }
    }
}
