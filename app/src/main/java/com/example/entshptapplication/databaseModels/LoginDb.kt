package com.example.entshptapplication.databaseModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.entshptapplication.dao.Converters
import org.jetbrains.annotations.NotNull
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "logins")
@TypeConverters(Converters::class)
data class LoginDb(
    @PrimaryKey(autoGenerate = true)  val id: Int,
    val LoginDate: LocalDate,
    val Workerid: Int,
    val num: String,
    val fio: String,
    val dolgnostId: Int,
    val isLogout: Boolean = false
)
