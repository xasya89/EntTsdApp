package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.models.FindNaryadModel
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.models.RequestUpakModel
import com.example.entshptapplication.models.ResponseMessageModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class UpakViewModel constructor(private val upakApi: UpakApi) : ViewModel() {

    val upakList = MutableLiveData<List<Naryad>>(listOf())
    val responseError = MutableLiveData<String>("")

    fun scan(barcode:String){
        val resp = upakApi.Scan(barcode)
        resp.enqueue(object : Callback<Naryad>{
            override fun onResponse(call: Call<Naryad>, response: Response<Naryad>) {
                if(response.isSuccessful){
                    val naryad = response.body()
                    if(naryad!=null){
                        var flag = false;
                        if(upakList.value!=null)
                            for (n in upakList.value?.iterator()!!)
                                if(n.id==naryad.id)
                                    flag = true
                        if(!flag)
                            upakList.value = upakList.value?.plus(naryad) ?: listOf(naryad)

                    }
                }
            }

            override fun onFailure(call: Call<Naryad>, t: Throwable) {
                responseError.value=t.message
            }

        })
    }

    fun addFindCheckedNaryads(findNaryads: List<FindNaryadModel>){
        var naryads = upakList.value
        for (findNaryad in findNaryads.filter { it.onChecked })
            if(naryads!!.none { it.id==findNaryad.id }){
                val newNaryad = Naryad(
                    id=findNaryad.id,
                    doorId = 0,
                    numInOrder = findNaryad.numInOrder,
                    num = findNaryad.naryadNum,
                    note = findNaryad.note ?: "",
                    shtild = findNaryad.shtild,
                    upakNaryadCompliteId = null, upakWorker = null, upakWorkerId = null, upakDate = null, upakCost = findNaryad.upakCost,
                    shptNaryadCompliteId = null, shptWorker = null, shptWorkerId = null, shptDate = null, shptCost = findNaryad.shptCost
                )
                naryads = naryads.plus(newNaryad)
            }
        upakList.value = naryads ?: listOf()
    }

    fun save(workerId: Int){
        var naryadsId = listOf<Int>()
        for(upak in upakList.value?.listIterator()!!)
            naryadsId = naryadsId.plus(upak.id)
        val resp = upakApi.Save(RequestUpakModel(naryads = naryadsId, workerId = workerId))
        resp.enqueue(object : Callback<ResponseMessageModel>{
            override fun onResponse(
                call: Call<ResponseMessageModel>,
                response: Response<ResponseMessageModel>
            ) {
                val body = response.body()
                if(body?.status=="error")
                    responseError.value = body.message
                if(body?.status=="ok") {
                    responseError.value = "ok"
                    clearUpakList()
                }
            }

            override fun onFailure(call: Call<ResponseMessageModel>, t: Throwable) {
                responseError.value= t.message
            }

        })
    }

    fun clearUpakList(){
        upakList.value = listOf<Naryad>()
    }
}