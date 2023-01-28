package com.example.entshptapplication.databaseModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.entshptapplication.dao.Converters
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "upak_naryads")
@TypeConverters( Converters::class)
data class UpakNaryadDb (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val naryadId: Int,
    val doorId: Int,
    val numInOrder: Int,
    val shet: String,
    val shetDateStr: String?,
    val num: String,
    val note: String,
    val shtild: String?,

    val upakNaryadCompliteId: Int,
    val upakWorkerId: Int=0,
    val upakDate: Date,
    val upakCost: Float
)