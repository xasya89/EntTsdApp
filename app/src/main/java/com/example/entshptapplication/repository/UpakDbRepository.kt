package com.example.entshptapplication.repository

import androidx.annotation.WorkerThread
import com.example.entshptapplication.dao.UpakDao
import com.example.entshptapplication.databaseModels.UpakNaryadDb
import com.example.entshptapplication.models.ConnectionSetting
import com.example.entshptapplication.models.Naryad
import kotlinx.coroutines.flow.Flow

class UpakDbRepository(private val upakDao: UpakDao) {
    fun getList(): Flow<List<UpakNaryadDb>>{
        return upakDao.getAll()
    }

    fun get(filter: String):Flow<List<UpakNaryadDb>>{
        return upakDao.get("%"+filter+"%")
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun Insert(naryadDb: UpakNaryadDb){
        upakDao.insert(naryadDb)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun Delete(naryadId:Int){
        upakDao.delete(naryadId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun clear(){
        upakDao.clear()
    }
}