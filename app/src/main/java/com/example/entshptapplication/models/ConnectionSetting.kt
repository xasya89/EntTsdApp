package com.example.entshptapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "connectionsettings")
data class ConnectionSetting(
    @PrimaryKey(autoGenerate = true) val uuid: Int = 0,
    val ServerHost: String
)
