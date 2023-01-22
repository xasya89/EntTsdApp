package com.example.entshptapplication.viewmodels

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.entshptapplication.communications.StatApi
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.models.StatNaryad
import com.example.entshptapplication.models.StatSummary
import com.example.entshptapplication.repository.UpakDbRepository
import retrofit2.Callback
import retrofit2.Response

class StatViewModel(val statApi: StatApi, onError: ((String)->Unit)? = null): ViewModel() {
    val summary = MutableLiveData<StatSummary>()
    val upakNaryadList = MutableLiveData<List<StatNaryad>>(listOf())
    val shptNaryadList = MutableLiveData<List<StatNaryad>>(listOf())

    fun getSummary(idWorker: Int){
        statApi.getSummary(idWorker).enqueue(object : Callback<StatSummary>{
            override fun onResponse(
                call: retrofit2.Call<StatSummary>,
                response: Response<StatSummary>
            ) {
                if(response.isSuccessful)
                    summary.value = response.body()
            }

            override fun onFailure(call: retrofit2.Call<StatSummary>, t: Throwable) {
                Log.e("Get summary response error", t.message.toString())
            }

        })
    }

    fun getUpakList(idWorker: Int){
        statApi.getUpakNaryads(idWorker).enqueue(object: Callback<List<StatNaryad>>{
            override fun onResponse(
                call: retrofit2.Call<List<StatNaryad>>,
                response: Response<List<StatNaryad>>
            ) {
                if(response.isSuccessful)
                    upakNaryadList.value = response.body()
            }

            override fun onFailure(call: retrofit2.Call<List<StatNaryad>>, t: Throwable) {}
        })
    }

    fun getShptList(idWorker: Int){
        statApi.getUpakNaryads(idWorker).enqueue(object: Callback<List<StatNaryad>>{
            override fun onResponse(
                call: retrofit2.Call<List<StatNaryad>>,
                response: Response<List<StatNaryad>>
            ) {
                if(response.isSuccessful)
                    upakNaryadList.value = response.body()
            }

            override fun onFailure(call: retrofit2.Call<List<StatNaryad>>, t: Throwable) {}
        })
    }
}

class StatViewModelFactory constructor(private val statApi: StatApi, var onError: ((String)-> Unit)): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatViewModel(statApi, onError) as T
    }
}