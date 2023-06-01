package com.example.entshptapplication.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.databaseModels.UpakNaryadDb
import com.example.entshptapplication.models.*
import com.example.entshptapplication.repository.UpakDbRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import kotlin.math.log

class UpakViewModel (private val upakApi: UpakApi, private val upakDbRepository: UpakDbRepository) : ViewModel() {
    val upakList = MutableLiveData<List<Naryad>>(listOf())
    val error = MutableLiveData<String?>(null)
    val scanResult = MutableLiveData<String>("")

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            error.postValue(throwable.message.toString())
        }
    }

    fun loadFromDb(){
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            val list = getUpakDb("")
            upakList.postValue(list)
        }
    }

    fun search(find: String){
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            val list = getUpakDb(find)
            upakList.postValue(list)
        }
    }

    private suspend fun getUpakDb(find: String) = withContext(Dispatchers.IO){
        return@withContext upakDbRepository.get(find).map { naryad ->
            Naryad(
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
        }
    }

    fun addFindCheckedNaryads(findNaryads: List<FindNaryadModel>, onComplite: (()->Unit) ) {
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            val newNaryads = findNaryads.filter { naryad -> upakList.value!!.none { naryad.id == it.id } }.map {naryad ->
                Naryad(
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
                )
            }

            addInDb(newNaryads)
            val list = getUpakDb("")
            upakList.postValue(list)

            onComplite.invoke()
        }
    }

    fun scan(barcode:String){
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            try{
                val naryad = upakApi.Scan(barcode)
                if(!upakList.value!!.none { it.id == naryad.id })
                    return@launch
                addInDb(listOf(naryad))


                val list = getUpakDb("")
                upakList.postValue(list)
                scanResult.postValue("success")
            }
            catch (ex:Exception){
                scanResult.postValue("error")
            }
        }
    }

    private suspend fun addInDb(naryads: List<Naryad>) = withContext(Dispatchers.IO) {
        upakDbRepository.InsertAll(naryads.map { naryad ->
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
        })
    }

    fun deleteNaryad(naryad: Naryad){
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            deleteInDb(naryad.id)
            upakList.postValue(upakList.value!!.filter { it.id != naryad.id })
        }
    }

    private suspend fun deleteInDb(naryadId:Int) = withContext(Dispatchers.IO){
        upakDbRepository.Delete(naryadId)
    }

    fun save(workerId: Int, onSuccess: (()->Unit)) {
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            upakApi.Save(RequestUpakModel(naryads = upakList.value!!.map { it.id }, workerId = workerId))
            upakDbRepository.clear()
            upakList.postValue(listOf())
            onSuccess.invoke()
        }
    }

    fun clearUpakList(onSuccess: (()->Unit)? = null){
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            upakDbRepository.clear()
            onSuccess?.invoke()
        }
        upakList.value = listOf<Naryad>()
    }
}

class UpakViewModelFactory constructor(private val upakApi: UpakApi, private val repository: UpakDbRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpakViewModel(upakApi, repository) as T
    }
}