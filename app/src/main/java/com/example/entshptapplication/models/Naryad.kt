package com.example.entshptapplication.models

import java.util.Date

data class Naryad(
    val id: Int,
    val shet: String,
    val shetDateStr: String?,
    val doorId: Int,
    val numInOrder: Int,
    val num: String,
    val note: String,
    val shtild: String?,

    val upakNaryadCompliteId: Int?,
    val upakWorker: Int?,
    val upakWorkerId: Int?,
    val upakDate: Date?,
    val upakCost: Float,

    val shptNaryadCompliteId: Int?,
    val shptWorker: Int?,
    val shptWorkerId: Int?,
    val shptDate: Date?,
    val shptCost: Float,
)
