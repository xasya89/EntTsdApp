package com.example.entshptapplication.ui.login

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.di.TSDApplication
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.dao.LoginDao
import com.example.entshptapplication.databaseModels.LoginDb
import com.example.entshptapplication.models.LoginModel
import com.example.entshptapplication.models.Worker
import com.example.entshptapplication.repository.LoginDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginApi: LoginApi,
    private val loginDao: LoginDao
) : ViewModel() {

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
                loginDao.Insert(LoginDb(0, LocalDate.now(), _worker.id, _worker.num, _worker.fio, _worker.dolgnostId))
                worker.postValue(_worker)
        }
    }

    fun getLogin(){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            val current = LocalDate.now()
            val dbLogin = loginDao.GetLogin(current)
            if(dbLogin!=null)
                worker.postValue(Worker(dbLogin.Workerid, dbLogin.num, dbLogin.fio, dbLogin.dolgnostId))
        }
    }

    fun logOut(){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            loginDao.Logout()
            worker.postValue(null)
        }
    }
}