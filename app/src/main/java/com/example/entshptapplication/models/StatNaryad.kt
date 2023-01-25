package com.example.entshptapplication.models

import java.util.*

data class StatNaryad(
    val naryadId: Int,
    val doorId: Int,
    val shet:String,
    val shetDateStr:String,
    val naryadNum:String,
    val numInOrder: Int,
    val note: String,
    val shtild: String?,

    val naryadCompliteId: Int,
    val worker: Int?,
    val workerId: Int,
    val dateCompliteStr: String,
    val cost: Float,
)
