package com.example.entshptapplication.upak.repository

import com.example.entshptapplication.upak.dao.UpakDao
import com.example.entshptapplication.upak.dbModels.UpakNaryadDb

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