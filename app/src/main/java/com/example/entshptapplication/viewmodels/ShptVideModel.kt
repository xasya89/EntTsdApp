package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.models.ActShpt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShptViewModel constructor(val shptApi: ShptApi, var onError: ((String)-> Unit)? = null) : ViewModel() {
    var shptList = MutableLiveData<List<ActShpt>>(listOf())

    fun getActList(){
        val resp = shptApi.GetActList()
        resp.enqueue(object: Callback<List<ActShpt>>{
            override fun onResponse(call: Call<List<ActShpt>>, response: Response<List<ActShpt>>) {
                if(!response.isSuccessful){
                    onError?.invoke(response.errorBody()?.string() ?: "ошибка")
                    return
                }
                shptList.value = response.body()
            }

            override fun onFailure(call: Call<List<ActShpt>>, t: Throwable) {
                onError?.invoke(t.message.toString());
            }
        })
    }

}

