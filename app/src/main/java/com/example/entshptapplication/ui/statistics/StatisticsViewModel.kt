package com.example.entshptapplication.ui.statistics

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.ui.naryadInfo.NaryadInfoViewModel
import com.example.entshptapplication.ui.naryadInfo.api.NaryadInfoApi
import com.example.entshptapplication.ui.statistics.communications.StatisticsApi
import com.example.entshptapplication.ui.statistics.models.CreateModel
import com.example.entshptapplication.ui.statistics.models.SummaryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class StatisticsViewModel(val statisticsApi: StatisticsApi): ViewModel() {
    val summary = MutableLiveData<SummaryModel?>(null)
    private val uuidJob = MutableLiveData<String>("")
    fun addJob(workerId: Int){
        summary.value = null
        viewModelScope.launch (Dispatchers.IO){
            val uuid = statisticsApi.addJob(CreateModel(workerId)).uuid
            uuidJob.postValue(uuid)
        }
    }

    fun getResult(){
        val uuid = uuidJob.value
        if(uuid==null || uuid=="") return
        viewModelScope.launch (Dispatchers.IO) {
            val response = statisticsApi.getStatistic(uuid)
            if(response.code()==HttpURLConnection.HTTP_OK && response.body()!=null){
                Log.d("Response", "Success")
                summary.postValue(response.body())
            }else
                Log.d("Response", response.body().toString())
            //summary.postValue(result)
        }
    }
}

class StatisticsViewModelFactory constructor(private val api: StatisticsApi): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatisticsViewModel(this.api) as T
    }

    companion object{
        fun Create(fragment: Fragment): StatisticsViewModel {
            val api = StatisticsApi.getInstance(HOSTED_NAME)
            return ViewModelProvider(fragment.requireActivity().viewModelStore, StatisticsViewModelFactory(api)).get(
                StatisticsViewModel::class.java)
        }
    }
}
