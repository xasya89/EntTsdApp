package com.example.entshptapplication.dao

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.entshptapplication.models.ConnectionSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM connectionsettings")
    fun getOne(): Flow<ConnectionSetting>

    @Query("SELECT * FROM connectionsettings")
    fun getList(): Flow<List<ConnectionSetting>>

    @Insert
    suspend fun insertSetting(vararg connectionSetting: ConnectionSetting)

    @Query("DELETE FROM connectionsettings")
    suspend fun deleteAllSettings()
}