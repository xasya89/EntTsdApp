package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.databaseModels.ShptDoorDb
import com.example.entshptapplication.models.*
import com.example.entshptapplication.repository.ShptDbRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShptOneViewModel(val shptApi: ShptApi, val shptDbRepository: ShptDbRepository): ViewModel() {
    val naryads = MutableLiveData<List<ActShptDoor>>(listOf())
    val error = MutableLiveData<String?>()

    fun getOActOne(idAct: Int) = liveData<ActShpt> {
        try {
            naryads.value = listOf()
            val act = load(idAct)
            naryads.value = act.doors
            emit(act)
        }
        catch (e: Exception) {
            error.postValue(e.message.toString())
        }
    }

    fun <T> concatenate(vararg lists: List<T>): List<T> {
        return listOf(*lists).flatten()
    }

    private suspend fun load(idAct: Int): ActShpt {
        val result = loadApi(idAct, "")
        val dbList = loadFromDb(idAct)
            .filter { dbDoor -> result.doors.none { it.idNaryad == dbDoor.naryadId } }
            .map { db ->
                ActShptDoor(
                    db.id,
                    db.naryadId,
                    db.doorId,
                    db.shet,
                    db.shetDateStr,
                    db.numInOrder,
                    db.num,
                    db.note,
                    db.shtild,
                    db.upakComplite,
                    db.shptComplite,
                    true
                )
            }
        result.doors = concatenate(result.doors, dbList)
        return result
    }

    private suspend fun loadApi(id: Int, find:String) = withContext(Dispatchers.IO){
        return@withContext shptApi.GetOne(id, find)
    }

    private suspend fun loadFromDb(actId: Int) = withContext(Dispatchers.IO){
        shptDbRepository.getList(actId)
    }

    fun scan(actId: Int, barCode: String, workerId: Int) {
        viewModelScope.launch(Dispatchers.IO + getCoroutineExceptionHandler()) {
            val newDoor = shptApi.Scan(workerId, barCode)
            newDoor.isInDb = true
            //naryads.postValue(concatenate(naryads.value!!, listOf(newDoor)))
            addInDb(actId, listOf(newDoor))
        }
    }

    fun chooseList(actId: Int, naryadsId: List<Int>, workerId: Int, onSuccess: (()-> Unit)){
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            if (naryadsId.size == 0)
                return@launch
            var naryadsIdStr = ""
            for (naryad in naryadsId)
                naryadsIdStr =
                    naryadsIdStr + naryad + (if (naryad != naryadsId.get(naryadsId.size - 1)) "," else "")
            withContext(Dispatchers.IO) {
                val listNaryads = naryads.value!!
                var doorsAdded = shptApi.getNaryadList(workerId, naryadsIdStr)
                    .filter { door -> listNaryads.none { it.idNaryad == door.idNaryad } == true }
                addInDb(actId, doorsAdded)
            }
            onSuccess.invoke()
        }
    }

    fun complite(actId: Int, workerId: Int, onSuccess: () -> Unit){
        viewModelScope.launch(Dispatchers.IO + getCoroutineExceptionHandler()) {
            shptApi.Complite(ShptCompliteListRequestPayload(
                actId,
                "",
                naryads.value!!.filter { it.isInDb }.map { it.idNaryad },
                workerId
            )
            )
            shptDbRepository.clear(actId)
            naryads.postValue(load(actId).doors)
        }
    }

    fun cancelComplite(actId: Int, onSuccess: () -> Unit){
        viewModelScope.launch {
            shptDbRepository.clear(actId)
            onSuccess.invoke()
        }
    }

    private suspend fun deleteFromServer(actDoorId: Int, workerId: Int) = withContext(Dispatchers.IO){
        shptApi.Delete(ShptCompliteDeleteRequestPayload(actDoorId=actDoorId, workerId= workerId))
    }

    private suspend fun deleteFromDb(actId: Int, naraydId: Int, workerId: Int) = withContext(Dispatchers.IO) {
        shptDbRepository.Delete(actId, naraydId)
    }

    fun delete(actId: Int, door: ActShptDoor, workerId: Int){
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            if (door.isInDb)
                deleteFromDb(actId, door.idNaryad, workerId)
            else
                deleteFromServer(door.id, workerId)
            naryads.value = naryads.value!!.filter { it.idNaryad != door.idNaryad }
        }
    }

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            error.postValue(throwable.message.toString())
        }
    }

    private suspend fun addInDb(actId: Int, doors: List<ActShptDoor>) = withContext(Dispatchers.IO) {
        val newDoors = doors.filter { door -> naryads.value!!.none { it.idNaryad == door.idNaryad } }.map {door ->
            ShptDoorDb(
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
        }
        if(newDoors.size==0)
            return@withContext

        shptDbRepository.InsertAll(newDoors)
        val naryadInDb = loadFromDb(actId)
        naryads.postValue(concatenate(
            naryads.value!!,
            naryadInDb.filter { door -> !newDoors.none { it.naryadId==door.naryadId } }.map { db ->
                ActShptDoor(
                    db.id,
                    db.naryadId,
                    db.doorId,
                    db.shet,
                    db.shetDateStr,
                    db.numInOrder,
                    db.num,
                    db.note,
                    db.shtild,
                    db.upakComplite,
                    db.shptComplite,
                    true
                )
            }
        ))
    }
}

class ShptOneViewModelFactory constructor(private val shptApi: ShptApi, private val shptDbRepository: ShptDbRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShptOneViewModel(shptApi, shptDbRepository) as T
    }
}
