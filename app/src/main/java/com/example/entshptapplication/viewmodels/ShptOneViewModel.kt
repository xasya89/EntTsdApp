package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.databaseModels.ShptDoorDb
import com.example.entshptapplication.models.*
import com.example.entshptapplication.repository.ShptDbRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShptOneViewModel(val shptApi: ShptApi, val shptDbRepository: ShptDbRepository, var onError: ((String)->Unit)? = null): ViewModel() {
    var act = MutableLiveData<ActShpt>(null)
    val naryads = MutableLiveData<MutableList<ActShptDoor>>(mutableListOf())
    val naryadsInDb = MutableLiveData<MutableList<ShptDoorDb>>(mutableListOf())

    fun getOActOne(idAct: Int, find: String){
        val resp = shptApi.GetOne(idAct, find)
        resp.enqueue(object : Callback<ActShpt>{
            override fun onResponse(call: Call<ActShpt>, response: Response<ActShpt>) {
                if (!response.isSuccessful){
                    onError?.invoke("ошибка открытия акта")
                    return
                }
                act.value = response.body()
                naryads.value = act.value!!.doors.toMutableList()
                loadFromDb(idAct)
            }

            override fun onFailure(call: Call<ActShpt>, t: Throwable) {
                onError?.invoke(t.message.toString())
            }

        })
    }

    fun loadFromDb(actId: Int){
        viewModelScope.launch {

            shptDbRepository.getList(actId).collect{
                val list = mutableListOf<ActShptDoor>()
                for(door in it)
                    if(naryads.value?.none { it.idNaryad == door.naryadId }==true )
                        list.add(ActShptDoor(
                            id = 0, idNaryad = door.naryadId, doorId = door.doorId, shet = door.shet, shetDateStr = door.shetDateStr, numInOrder = door.numInOrder, num = door.num,
                            note = door.note, shtild = door.shtild, upakComplite = door.upakComplite, shptComplite = door.shptComplite, isInDb = true
                        ))
                list.addAll(naryads.value!!.toList())
                naryads.postValue(list)
                naryadsInDb.postValue(it.toMutableList())

            }
        }
    }

    fun scan(actId: Int, barCode: String, workerId: Int){
        val resp = shptApi.Scan(workerId, barCode)
        resp.enqueue(object : Callback<ActShptDoor>{
            override fun onResponse(call: Call<ActShptDoor>, response: Response<ActShptDoor>) {
                if(!response.isSuccessful){
                    onError?.invoke(response.errorBody()?.string() ?: "ошибка")
                    return
                }
                val list = naryads.value ?: mutableListOf()
                if(list.none{it.idNaryad==response.body()?.idNaryad})
                    list.add(0,response.body()!!)
                naryads.postValue(list)
                addInDb(actId, response.body()!!)
            }

            override fun onFailure(call: Call<ActShptDoor>, t: Throwable) {
                onError?.invoke(t.message.toString())
            }

        })
    }

    fun chooseList(actId: Int, naryadsId: List<Int>, workerId: Int, onSuccess: (()-> Unit)){
        if(naryadsId.size == 0)
            return
        var naryadsIdStr = ""
        for(naryad in naryadsId)
            naryadsIdStr = naryadsIdStr + naryad + (if(naryad != naryadsId.get(naryadsId.size - 1)) "," else "")

        val resp = shptApi.getNaryadList(workerId, naryadsIdStr)
        resp.enqueue(object: Callback<List<ActShptDoor>>{
            override fun onResponse(
                call: Call<List<ActShptDoor>>,
                response: Response<List<ActShptDoor>>
            ) {
                if(response.isSuccessful==false){
                    onError?.invoke( response.errorBody()?.string() ?: "ошибка" )
                    return
                }
                //Добавим в общий список
                val list = mutableListOf<ActShptDoor>()
                val listNaryads = naryads.value!!
                for(door in response.body()!!.listIterator()) {
                    if (listNaryads.none { it.idNaryad == door.idNaryad } == true) {
                        door.isInDb = true
                        list.add(0, door)
                    }
                }
                listNaryads.addAll(0,list)
                naryads.postValue(listNaryads)
                addInDb(actId, response.body()!!, onSuccess)
            }

            override fun onFailure(call: Call<List<ActShptDoor>>, t: Throwable) {
                onError?.invoke(t.message.toString())
            }

        })
    }

    fun complite(actId: Int, workerId: Int, onSuccess: () -> Unit){
        val doors = naryadsInDb.value!!
        val naryadsId = mutableListOf<Int>()
        for(door in doors)
            naryadsId.add(door.naryadId)
        val model = ShptCompliteListRequestPayload(actId, "", naryadsId, workerId)
        val resp = shptApi.Complite(model)
        resp.enqueue(object : Callback<List<ActShptDoor>>{
            override fun onResponse(
                call: Call<List<ActShptDoor>>,
                response: Response<List<ActShptDoor>>
            ) {
                if(!response.isSuccessful){
                    onError?.invoke(response.errorBody()?.string() ?: "ошибка")
                    return
                }
                viewModelScope.launch {
                    shptDbRepository.clear(actId)
                    onSuccess.invoke()
                }
            }

            override fun onFailure(call: Call<List<ActShptDoor>>, t: Throwable) {
                onError?.invoke(t.message.toString())
            }

        })
    }

    fun cancelComplite(actId: Int, onSuccess: () -> Unit){
        viewModelScope.launch {
            shptDbRepository.clear(actId)
            onSuccess.invoke()
        }
    }

    private fun deleteFromServer(actDoorId: Int, workerId: Int){
        val resp = shptApi.Delete(ShptCompliteDeleteRequestPayload(actDoorId=actDoorId, workerId= workerId))
        resp.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(!response.isSuccessful){
                    onError?.invoke(response.errorBody()?.string() ?: "ошибка")
                    return
                }
                naryads.postValue(naryads.value!!.filter { it.id != actDoorId }.toMutableList())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onError?.invoke(t.message.toString())
            }

        })
    }

    private fun deleteFromDb(actId: Int, naraydId: Int, workerId: Int){
        viewModelScope.launch {
            shptDbRepository.Delete(actId, naraydId)
            var list = naryads.value!!
            list = list.filter { it.idNaryad!=naraydId }.toMutableList()
            naryads.postValue(list)

            var listdb = naryadsInDb.value!!
            listdb = listdb.filter { it.naryadId!=naraydId }.toMutableList()
            naryadsInDb.postValue(listdb)
        }
    }

    fun delete(actId: Int, door: ActShptDoor, workerId: Int){
        if(door.isInDb)
            deleteFromDb(actId, door.idNaryad, workerId)
        else
            deleteFromServer(door.id, workerId)
    }

    private fun addInDb(actId: Int, door: ActShptDoor){
        viewModelScope.launch {
            val shptDoorDb = ShptDoorDb(
                id = 0,
                actId = actId,
                naryadId = door.idNaryad,
                doorId = door.doorId,
                shet = door.shet,
                shetDateStr = door.shetDateStr,
                numInOrder = door.numInOrder,
                num = door.num,
                note = door.note,
                shtild = door.shtild,
                upakComplite = door.upakComplite,
                shptComplite = door.shptComplite
            )
            shptDbRepository.Insert(shptDoorDb)
            val list = naryadsInDb.value
            list?.add(shptDoorDb)
            naryadsInDb.postValue(list ?: mutableListOf())
        }
    }

    private fun addInDb(actId: Int, doors: List<ActShptDoor>, onSuccess: (() -> Unit)? = null){
        viewModelScope.launch {
            val list = naryadsInDb.value
            for(door in doors){
                val shptDoorDb = ShptDoorDb(
                    id = 0,
                    actId = actId,
                    naryadId = door.idNaryad,
                    doorId = door.doorId,
                    shet = door.shet,
                    shetDateStr = door.shetDateStr,
                    numInOrder = door.numInOrder,
                    num = door.num,
                    note = door.note,
                    shtild = door.shtild,
                    upakComplite = door.upakComplite,
                    shptComplite = door.shptComplite
                )
                shptDbRepository.Insert(shptDoorDb)
                list?.add(shptDoorDb)
            }
            naryadsInDb.postValue(list ?: mutableListOf())
            onSuccess?.invoke()
        }
    }
}

class ShptOneViewModelFactory constructor(private val shptApi: ShptApi, private val shptDbRepository: ShptDbRepository, private var onError: ((String)->Unit)? = null): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShptOneViewModel(shptApi, shptDbRepository, onError) as T
    }
}