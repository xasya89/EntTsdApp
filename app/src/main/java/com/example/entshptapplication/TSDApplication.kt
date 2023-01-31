package com.example.entshptapplication

import android.app.Application
import com.example.entshptapplication.dao.AppDatabase
import com.example.entshptapplication.repository.SettingsDbRepository
import com.example.entshptapplication.repository.ShptDbRepository
import com.example.entshptapplication.repository.UpakDbRepository

class TSDApplication:Application() {

    val  database by lazy { AppDatabase.getDatabase(this) }
    val settingsDbRepository by lazy { SettingsDbRepository(database.settingsDao()) }
    val upakDbRepository by lazy { UpakDbRepository(database.upakDao()) }
    val shptDbRepository by lazy { ShptDbRepository(database.shptDao()) }
}