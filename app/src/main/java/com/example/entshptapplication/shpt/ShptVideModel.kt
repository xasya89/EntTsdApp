package com.example.entshptapplication.shpt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.shpt.api.ShptApi
import com.example.entshptapplication.shpt.models.ActShpt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

