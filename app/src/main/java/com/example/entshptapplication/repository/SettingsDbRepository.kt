package com.example.entshptapplication.repository

import androidx.annotation.WorkerThread
import com.example.entshptapplication.dao.SettingsDao
import com.example.entshptapplication.models.ConnectionSetting
import kotlinx.coroutines.flow.Flow

class SettingsDbRepository(private val settingsDao: SettingsDao) {

    val settings: Flow<List<ConnectionSetting>> = settingsDao.getList()
    val setting: Flow<ConnectionSetting> = settingsDao.getOne()

    suspend fun getSetting() = settingsDao.getSetting()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(connectionSetting: ConnectionSetting){
        settingsDao.insertSetting(connectionSetting)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun clear(){
        settingsDao.deleteAllSettings()
    }
}