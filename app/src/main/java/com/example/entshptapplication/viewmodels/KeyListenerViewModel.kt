package com.example.entshptapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KeyListenerViewModel:ViewModel() {
    var barCode = MutableLiveData<String>()
}

class KeyListenerViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return KeyListenerViewModel() as T
    }
}