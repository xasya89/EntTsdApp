package com.example.entshptapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.entshptapplication.communications.FindNaryadsApi
import com.example.entshptapplication.models.FindNaryadModel
import com.example.entshptapplication.models.Naryad
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindNaryadsViewModel constructor(private val findNaryadsApi: FindNaryadsApi): ViewModel() {
    val findNaryadList = MutableLiveData<List<FindNaryadModel>>(listOf())
    val naryad = MutableLiveData<FindNaryadModel?>(null)

    fun find(findStr:String){
        val resp = findNaryadsApi.Find(findStr)
        findNaryadList.value= listOf()
        resp.enqueue(object: Callback<List<FindNaryadModel>>{
            override fun onResponse(call: Call<List<FindNaryadModel>>, response: Response<List<FindNaryadModel>>) {
                if(response.isSuccessful)
                    findNaryadList.value = response.body() ?: listOf<FindNaryadModel>()
            }

            override fun onFailure(call: Call<List<FindNaryadModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun get(naryadId: Int){
        var resp = findNaryadsApi.GetNaryad(naryadId)
        naryad.value = null
        resp.enqueue(object: Callback<FindNaryadModel>{
            override fun onResponse(
                call: Call<FindNaryadModel>,
                response: Response<FindNaryadModel>
            ) {
                if(response.isSuccessful)
                    naryad.value = response.body()
            }

            override fun onFailure(call: Call<FindNaryadModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}