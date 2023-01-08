package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShptOneViewModel(val shptApi: ShptApi, var onError: ((String)->Unit)? = null): ViewModel() {
    var act = MutableLiveData<ActShpt>(null)

    fun getOActOne(idAct: Int, find: String){
        val resp = shptApi.GetOne(idAct, find)
        resp.enqueue(object : Callback<ActShpt>{
            override fun onResponse(call: Call<ActShpt>, response: Response<ActShpt>) {
                if (!response.isSuccessful)
                    onError?.invoke("ошибка открытия акта")
                act.value = response.body()
            }

            override fun onFailure(call: Call<ActShpt>, t: Throwable) {
                onError?.invoke(t.message.toString())
            }

        })
    }

    fun scan(actId: Int, barCode: String, workerId: Int){
        val resp = shptApi.Scan(ShptCompliteRequestPayload(actId = actId, barCode = barCode, workerId = workerId, naryadId = 0))
        resp.enqueue(object : Callback<ActShptDoor>{
            override fun onResponse(call: Call<ActShptDoor>, response: Response<ActShptDoor>) {
                if(!response.isSuccessful){
                    onError?.invoke(response.errorBody()?.string() ?: "ошибка")
                    return
                }
                val actChange = act.value!!
                actChange.doors = actChange!!.doors.plus(response.body()!!)
                actChange.doorCount = (actChange.doorCount ?: 0) + 1
                act.value = actChange
            }

            override fun onFailure(call: Call<ActShptDoor>, t: Throwable) {
                onError?.invoke(t.message.toString())
            }

        })
    }

    fun scanList(actId: Int, naryadsId: List<Int>, workerId: Int, onScanSUccess: (() -> Unit)?=null){
        if(naryadsId.size == 0){
            onScanSUccess?.invoke()
            return
        }
        val resp = shptApi.ScanList(ShptCompliteListRequestPayload(actId= actId, naryadsId=naryadsId, workerId=workerId))
        resp.enqueue(object: Callback<List<ActShptDoor>>{
            override fun onResponse(
                call: Call<List<ActShptDoor>>,
                response: Response<List<ActShptDoor>>
            ) {
                if(response.isSuccessful==false){
                    onError?.invoke( response.errorBody()?.string() ?: "ошибка" )
                    return
                }

                val actChange = act.value!!
                actChange.doors = actChange!!.doors.plus(response.body()!!)
                actChange.doorCount = actChange.doors.size
                act.value = actChange
                onScanSUccess?.invoke()
            }

            override fun onFailure(call: Call<List<ActShptDoor>>, t: Throwable) {
                onError?.invoke(t.message.toString())
            }

        })
    }

    fun delete(actDoorId: Int, workerId: Int){
        val resp = shptApi.Delete(ShptCompliteDeleteRequestPayload(actDoorId=actDoorId, workerId= workerId))
        resp.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(!response.isSuccessful){
                    onError?.invoke(response.errorBody()?.string() ?: "ошибка")
                    return
                }
                val actChange = act.value!!
                actChange.doors = actChange!!.doors.minus(actChange.doors.filter { it.id == actDoorId })
                actChange.doorCount = actChange.doors.size
                act.value = actChange
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onError?.invoke(t.message.toString())
            }

        })
    }
}

class ShptOneViewModelFactory constructor(private val shptApi: ShptApi, private var onError: ((String)->Unit)? = null): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShptOneViewModel(shptApi, onError) as T
    }
}