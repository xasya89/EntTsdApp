package com.example.entshptapplication.ui.statistics.models

import java.util.Date

data class NaryadStatisitcResponseModel(
    val naryadId: Int,
    val naryadCompliteId: Int,
    val shet: String,
    val shetDate: Date,
    val numInOrder: Int,
    val naryadNum: String,
    val cost: Double,
    val doorName: String,
    val h: Int,
    val w: Int,
    val s: Int?,
    val sEqual: Boolean
)
