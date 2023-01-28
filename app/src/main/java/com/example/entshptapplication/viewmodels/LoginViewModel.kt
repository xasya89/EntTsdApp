package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.LoginModel
import com.example.entshptapplication.models.Worker
import com.example.entshptapplication.repository.LoginRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.PrintWriter
import java.io.StringWriter

class LoginViewModel constructor(private val loginRepository: LoginRepository?) : ViewModel() {
    var login = MutableLiveData<Worker>()

    fun Authorize(model: LoginModel){

        val response = loginRepository?.Authorize(model)!!
        response.enqueue(object : Callback<Worker> {
            override fun onResponse(call: Call<Worker>, response: Response<Worker>) {
                if(response.isSuccessful)
                    login.postValue(response.body())
            }

            override fun onFailure(call: Call<Worker>, t: Throwable) {
                Log.e("Login viewmodel error", t.message.toString())
            }

        })
    }

    fun logOut(){
        login.value = Worker(id=0, num = "", fio = "", dolgnostId = 0)
    }
}