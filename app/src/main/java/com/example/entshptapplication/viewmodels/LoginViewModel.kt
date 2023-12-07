package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.TSDApplication
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.databaseModels.LoginDb
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.LoginModel
import com.example.entshptapplication.models.Worker
import com.example.entshptapplication.repository.LoginDbRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class LoginViewModel constructor(private val loginApi: LoginApi, private val loginDbRepository: LoginDbRepository) : ViewModel() {

    val error = MutableLiveData<String?>(null)
    var worker = MutableLiveData<Worker?>(null)

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            error.postValue(throwable.message.toString())
        }
    }
    fun authorize(smartCartNum: String){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
                val _worker = loginApi.Authorize(LoginModel(smartCartNum))
                loginDbRepository.Login(LoginDb(0, LocalDate.now(), _worker.id, _worker.num, _worker.fio, _worker.dolgnostId))
                worker.postValue(_worker)
        }
    }

    fun getLogin(){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            val dbLogin = loginDbRepository.GetLogin()
            if(dbLogin!=null)
                worker.postValue(Worker(dbLogin.Workerid, dbLogin.num, dbLogin.fio, dbLogin.dolgnostId))
        }
    }

    fun logOut(){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            loginDbRepository.Logout()
            worker.postValue(null)
        }
    }
}

class LoginViewModelFactory constructor(private val loginApi: LoginApi, private  val loginDbRepository: LoginDbRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(this.loginApi, this.loginDbRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

class LoginViewModelCreater{
    companion object{
        fun createViewModel(fragment: Fragment):LoginViewModel{
            val loginDbRepository = (fragment.requireActivity().application as TSDApplication).loginDbRepository
            val loginApi = LoginApi.getInstance("")
            Log.d("INIT_LOGIN_VIEWMODEL", "INIT")
            return ViewModelProvider(fragment.requireActivity().viewModelStore, LoginViewModelFactory(
                loginApi, loginDbRepository
            )).get(LoginViewModel::class.java)
        }
    }
}