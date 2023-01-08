package com.example.entshptapplication

import android.app.Application
import com.example.entshptapplication.dao.AppDatabase
import com.example.entshptapplication.repository.SettingsDbRepository

class TSDApplication:Application() {

    val  database by lazy { AppDatabase.getDatabase(this) }
    val settingsDbRepository by lazy { SettingsDbRepository(database.settingsDao()) }
}