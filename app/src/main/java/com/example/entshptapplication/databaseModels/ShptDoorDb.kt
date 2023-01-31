package com.example.entshptapplication.databaseModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.entshptapplication.dao.Converters

@Entity(tableName = "shpt_doors")
@TypeConverters( Converters::class)
data class ShptDoorDb(
    @PrimaryKey(autoGenerate = true)  val id: Int,
    val actId: Int,
    val naryadId: Int,
    val doorId: Int,
    val shet: String,
    val shetDateStr: String,
    val numInOrder: Int,
    val num: String,
    val note: String?,
    val shtild: String?,
    val upakComplite: Boolean,
    val shptComplite: Boolean,
)
