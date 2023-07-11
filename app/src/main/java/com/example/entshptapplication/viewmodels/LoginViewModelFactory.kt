package com.example.entshptapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.communications.UpakApi



class ShptViewModelFactory constructor(private val shptApi: ShptApi, var onError: ((String)-> Unit)? = null): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShptViewModel(shptApi, onError) as T
    }
}