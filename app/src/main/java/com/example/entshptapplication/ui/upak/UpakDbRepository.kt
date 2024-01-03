package com.example.entshptapplication.ui.upak

import androidx.annotation.WorkerThread
import com.example.entshptapplication.dao.UpakDao
import com.example.entshptapplication.databaseModels.UpakNaryadDb
import com.example.entshptapplication.models.ConnectionSetting
import com.example.entshptapplication.models.Naryad
import kotlinx.coroutines.flow.Flow

class UpakDbRepository(private val upakDao: UpakDao) {
    suspend fun getList(): List<UpakNaryadDb>{
        return upakDao.getAll()
    }

    suspend fun get(filter: String):List<UpakNaryadDb>{
        return upakDao.get("%"+filter+"%")
    }

    suspend fun Insert(naryadDb: UpakNaryadDb){
        upakDao.insert(naryadDb)
    }

    suspend fun InsertAll(naryadsDb: List<UpakNaryadDb>){
        upakDao.insertAll(naryadsDb)
    }

    suspend fun Delete(naryadId:Int){
        upakDao.delete(naryadId)
    }

    suspend fun clear(){
        upakDao.clear()
    }
}