package com.example.entshptapplication.models

import java.util.*

data class StatNaryad(
    val id: Int,
    val doorId: Int,
    val shet:String,
    val shetDateStr:String,
    val numInOrder: Int,
    val num: String,
    val note: String,
    val shtild: String?,

    val naryadCompliteId: Int,
    val worker: Int?,
    val workerId: Int,
    val dateCompliteStr: String,
    val cost: Float,
)
