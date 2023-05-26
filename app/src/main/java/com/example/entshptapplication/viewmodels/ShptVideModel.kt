package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.models.ActShpt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShptViewModel constructor(val shptApi: ShptApi, var onError: ((String)-> Unit)? = null) : ViewModel() {

    fun getActList() = liveData<List<ActShpt>>(Dispatchers.IO) {

        try{
            emit(shptApi.GetActList())
        }
        catch (e:Exception){
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.Main){
                        onError?.invoke(e.message.toString())
                    }
                }
                catch (e:Exception){}
            }
        }
    }

}

