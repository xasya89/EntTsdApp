package com.example.entshptapplication.ui.shpt.shpt_one

import com.example.entshptapplication.ui.shpt.ShptDao
import com.example.entshptapplication.databaseModels.ShptDoorDb
import kotlinx.coroutines.flow.Flow

class ShptDbRepository1 (private val shptDao: ShptDao) {
    fun getAll(): Flow<List<ShptDoorDb>> {
        return shptDao.getAll()
    }

    suspend fun getList(actId: Int): List<ShptDoorDb> {
        return shptDao.getAll(actId)
    }

    fun get(actId:Int, filter: String): Flow<List<ShptDoorDb>> {
        return shptDao.get(actId, "%"+filter+"%")
    }

    //@Suppress("RedundantSuspendModifier")
    //@WorkerThread
    suspend fun Insert(shptDoorDb: ShptDoorDb){
        shptDao.insert(shptDoorDb)
    }

    suspend fun InsertAll(shptDoorsDb: List<ShptDoorDb>){
        shptDao.insertAll(shptDoorsDb)
    }

    //@Suppress("RedundantSuspendModifier")
    //@WorkerThread
    suspend fun Delete(actId:Int, naryadId:Int){
        shptDao.delete(actId, naryadId)
    }

    //@Suppress("RedundantSuspendModifier")
    //@WorkerThread
    suspend fun clear(actId: Int){
        shptDao.clear(actId)
    }
}