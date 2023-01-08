package com.example.entshptapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.repository.LoginRepository

class LoginViewModelFactory constructor(private val repository: LoginRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

class UpakViewModelFactory constructor(private val upakApi: UpakApi): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpakViewModel(upakApi) as T
    }
}

class ShptViewModelFactory constructor(private val shptApi: ShptApi, var onError: ((String)-> Unit)? = null): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShptViewModel(shptApi, onError) as T
    }
}