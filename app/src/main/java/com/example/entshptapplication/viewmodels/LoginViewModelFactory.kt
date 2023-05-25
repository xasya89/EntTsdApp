package com.example.entshptapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.communications.UpakApi

class LoginViewModelFactory constructor(private val loginApi: LoginApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(this.loginApi) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

class ShptViewModelFactory constructor(private val shptApi: ShptApi, var onError: ((String)-> Unit)? = null): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShptViewModel(shptApi, onError) as T
    }
}