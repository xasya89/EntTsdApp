package com.example.entshptapplication.ui.shpt.shpt_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.models.ActShpt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ShptViewModel @Inject constructor(
    val shptApi: ShptApi
) : ViewModel() {

    fun getActList() = liveData<List<ActShpt>>(Dispatchers.IO) {
        try{
            emit(shptApi.GetActList())
        }
        catch (e:Exception){
            Log.e("Shpt get list", e.message.toString())
        }
    }

}

