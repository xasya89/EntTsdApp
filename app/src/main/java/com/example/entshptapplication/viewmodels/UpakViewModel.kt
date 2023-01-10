package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.databaseModels.UpakNaryadDb
import com.example.entshptapplication.models.FindNaryadModel
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.models.RequestUpakModel
import com.example.entshptapplication.models.ResponseMessageModel
import com.example.entshptapplication.repository.UpakDbRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import kotlin.math.log

class UpakViewModel (private val upakApi: UpakApi, private val upakDbRepository: UpakDbRepository, private var onError: ((String)-> Unit)) : ViewModel() {

    val upakList = MutableLiveData<List<Naryad>>(listOf())

    fun loadFromDb(){
        viewModelScope.launch {
            upakDbRepository.getList().collect {
                val naryads = mutableListOf<Naryad>()
                for (naryad in it)
                    naryads.add(
                        0, Naryad(
                            id = naryad.naryadId,
                            doorId = naryad.doorId,
                            numInOrder = naryad.numInOrder,
                            num = naryad.num,
                            note = naryad.note,
                            shtild = naryad.shtild,

                            upakNaryadCompliteId = naryad.upakNaryadCompliteId,
                            upakCost = naryad.upakCost,
                            upakDate = null,
                            upakWorker = null,
                            upakWorkerId = null,
                            shptNaryadCompliteId = null,
                            shptWorkerId = null,
                            shptDate = null,
                            shptWorker = null,
                            shptCost = 0f
                        )
                    )
                upakList.value = naryads
            }
        }
    }

    fun scan(barcode:String){
        val resp = upakApi.Scan(barcode)
        resp.enqueue(object : Callback<Naryad>{
            override fun onResponse(call: Call<Naryad>, response: Response<Naryad>) {
                if (!response.isSuccessful) {
                    onError(response.errorBody()?.string() ?: "")
                    return
                }
                val naryad = response.body()
                if (naryad == null)
                    return
                if(upakList.value?.none{it.id==naryad.id} ?: false)
                    return
                addInDb(naryad)

                upakList.value = upakList.value?.plus(naryad)
            }

            override fun onFailure(call: Call<Naryad>, t: Throwable) {
                onError(t.message.toString())
            }

        })
    }

    fun addFindCheckedNaryads(findNaryads: List<FindNaryadModel>, onComplite: (()->Unit) ) {
        var naryads = upakList.value
        val findNaryads_ = findNaryads.filter { x-> x.onChecked and (naryads?.none{n->n.id==x.id} ?: true) }
        val tmplist = mutableListOf<Naryad>()
        for(naryad in findNaryads_)
            tmplist.add(Naryad(
                id = naryad.id,
                doorId = 0,
                numInOrder = naryad.numInOrder,
                num = naryad.naryadNum,
                note = naryad.note ?: "",
                shtild = naryad.shtild,
                upakNaryadCompliteId = null,
                upakWorker = null,
                upakWorkerId = null,
                upakDate = null,
                upakCost = naryad.upakCost,
                shptNaryadCompliteId = null,
                shptWorker = null,
                shptWorkerId = null,
                shptDate = null,
                shptCost = naryad.shptCost
            ))
        addInDb(tmplist, onComplite)
    }

    private fun addInDb(naryad: Naryad){
        viewModelScope.launch {
            upakDbRepository.Insert(UpakNaryadDb(
                id=0,
                naryadId = naryad.id,
                doorId = naryad.doorId,
                numInOrder = naryad.numInOrder,
                num = naryad.num,
                note = naryad.note,
                shtild = naryad.shtild,
                upakNaryadCompliteId = naryad.id,
                upakCost = naryad.upakCost,

                upakWorkerId = 0, upakDate = Date()
            ))

        }
    }

    private fun addInDb(naryads: List<Naryad>, onComplite: (() -> Unit)? = null) {
        viewModelScope.launch {
            for (naryad in naryads)
                upakDbRepository.Insert(
                    UpakNaryadDb(
                        id = 0,
                        naryadId = naryad.id,
                        doorId = naryad.doorId,
                        numInOrder = naryad.numInOrder,
                        num = naryad.num,
                        note = naryad.note,
                        shtild = naryad.shtild,
                        upakNaryadCompliteId = naryad.id,
                        upakCost = naryad.upakCost,

                        upakWorkerId = 0, upakDate = Date()
                    )
                )
            upakList.value = naryads.plus(naryads)
            onComplite?.invoke()
        }
    }

    fun save(workerId: Int) {
        var naryadsId = listOf<Int>()
        for (upak in upakList.value?.listIterator()!!)
            naryadsId = naryadsId.plus(upak.id)
        val resp = upakApi.Save(RequestUpakModel(naryads = naryadsId, workerId = workerId))
        resp.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!response.isSuccessful) {
                    onError(response.errorBody()?.string() ?: "")
                    return
                }

                clearUpakList()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onError(t.message.toString())
            }

        })
    }

    fun clearUpakList(){
        viewModelScope.launch {
            upakDbRepository.clear()
        }
        upakList.value = listOf<Naryad>()
    }
}

class UpakViewModelFactory constructor(private val upakApi: UpakApi, private val repository: UpakDbRepository, var onError: ((String)-> Unit)): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpakViewModel(upakApi, repository, onError) as T
    }
}