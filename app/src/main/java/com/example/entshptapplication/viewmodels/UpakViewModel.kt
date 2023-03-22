package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.databaseModels.UpakNaryadDb
import com.example.entshptapplication.models.*
import com.example.entshptapplication.repository.UpakDbRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import kotlin.math.log

class UpakViewModel (private val upakApi: UpakApi, private val upakDbRepository: UpakDbRepository, private var onError: ((String)-> Unit)) : ViewModel() {

    val upakList = MutableLiveData<List<Naryad>>(listOf())
    val upakFilterList = MutableLiveData<List<Naryad>>(listOf())
    val filterStr = MutableLiveData<String>().apply { value = "" }
    fun updateFilterStr(str: String){
        filterStr.value = str
        filter()
    }

    fun loadFromDb(){
        viewModelScope.launch {
            upakDbRepository.getList().collect {
                updateUpakList(it)
                updateUpakFilterList(it)
            }
        }
    }

    fun filter() {
        viewModelScope.launch {
            upakDbRepository.get(filterStr.value ?: "").collect { updateUpakFilterList(it) }
        }
    }

    private fun updateUpakList(list: List<UpakNaryadDb>){
        val naryads = mutableListOf<Naryad>()
        for (naryad in list)
            naryads.add(
                0, Naryad(
                    id = naryad.naryadId,
                    shet = naryad.shet,
                    shetDateStr = naryad.shetDateStr,
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

    private fun updateUpakFilterList(list: List<UpakNaryadDb>){
        val naryads = mutableListOf<Naryad>()
        for (naryad in list)
            naryads.add(
                0, Naryad(
                    id = naryad.naryadId,
                    shet = naryad.shet,
                    shetDateStr = naryad.shetDateStr,
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
        upakFilterList.value = naryads
    }

    fun scan(barcode:String){
        val resp = upakApi.Scan(barcode)
        resp.enqueue(object : Callback<Naryad>{
            override fun onResponse(call: Call<Naryad>, response: Response<Naryad>) {
                Log.d("response", response.isSuccessful.toString())
                if (!response.isSuccessful) {
                    try {
                        val errorStringRaw: String? = response.errorBody()?.string()
                        val response: ResponseMessageModel = Gson().fromJson(errorStringRaw, ResponseMessageModel::class.java)
                        onError?.invoke( response.message ?: "")
                    } catch (e: Exception) {

                    }
                    return
                }
                val naryad = response.body()
                Log.d("naryad",naryad.toString())
                if (naryad == null)
                    return
                if(upakList.value?.any{it.id==naryad.id} == true)
                    return
                addInDb(naryad)
                upakList.value = upakList.value?.plus(naryad)
            }

            override fun onFailure(call: Call<Naryad>, t: Throwable) {
                onError?.invoke(t.message.toString())
                Log.d("error lof", t.message.toString())
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
                shet = naryad.shet,
                shetDateStr = naryad.shetDateStr,
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
                shet = naryad.shet,
                shetDateStr = naryad.shetDateStr,
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
                        shet = naryad.shet,
                        shetDateStr = naryad.shetDateStr,
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
            upakFilterList.value = naryads.filter { it.num.indexOf(filterStr.value ?: "") > -1 }
            onComplite?.invoke()
        }
    }

    fun deleteNaryad(naryad: Naryad){
        viewModelScope.launch {
            upakDbRepository.Delete(naryad.id)
            upakList.value = upakList.value?.minus(naryad) ?: listOf()
            upakFilterList.value = upakFilterList.value?.minus(naryad) ?: listOf()
        }
    }

    fun save(workerId: Int, onSuccess: (()->Unit)) {
        var naryadsId = listOf<Int>()
        if(upakList.value!=null)
        for (upak in upakList.value!!.listIterator())
            naryadsId = naryadsId.plus(upak.id)
        if(naryadsId.size==0){
            onSuccess.invoke()
            return
        }
        val resp = upakApi.Save(RequestUpakModel(naryads = naryadsId, workerId = workerId))
        resp.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful == false) {
                    onError?.invoke(response.errorBody()?.string() ?: "")
                    return
                }
                onSuccess.invoke()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("error upak save", t.message.toString())
                onError?.invoke(t.message.toString())
            }

        })


    }

    fun clearUpakList(onSuccess: (()->Unit)? = null){
        viewModelScope.launch {
            upakDbRepository.clear()
            onSuccess?.invoke()
        }
        upakList.value = listOf<Naryad>()
    }
}

class UpakViewModelFactory constructor(private val upakApi: UpakApi, private val repository: UpakDbRepository, var onError: ((String)-> Unit)): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpakViewModel(upakApi, repository, onError) as T
    }
}