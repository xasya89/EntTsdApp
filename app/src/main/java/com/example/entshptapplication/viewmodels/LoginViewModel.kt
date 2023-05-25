package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.LoginModel
import com.example.entshptapplication.models.Worker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.PrintWriter
import java.io.StringWriter

class LoginViewModel constructor(private val loginApi: LoginApi) : ViewModel() {
    private var _worker:Worker? = null
    fun getLogin(model: LoginModel) = liveData<Worker?>(Dispatchers.IO) {
        if(_worker!=null)
            emit(_worker);
        try{
            val worker = loginApi.Authorize(model)
            _worker = worker
            login.postValue(worker)
            emit(worker)
        }
        catch (e: Exception){
            Log.e("wrror", e.message.toString())
        }
    }

    var login = MutableLiveData<Worker>()

    fun logOut(){
        login.value = Worker(id=0, num = "", fio = "", dolgnostId = 0)
    }
}