package com.example.entshptapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.entshptapplication.shpt.ShptViewModel
import com.example.entshptapplication.shpt.api.ShptApi


class ShptViewModelFactory constructor(private val shptApi: ShptApi, var onError: ((String)-> Unit)? = null): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShptViewModel(shptApi, onError) as T
    }
}