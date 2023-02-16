package com.example.entshptapplication.models

import java.util.Date

data class StatSummary(
    val dateWithStr:String,
    val initialCost: Float,
    val upakCount: Int,
    val upakCost: Float,
    val shptCount: Int,
    val shptCost: Float,
    val paymentsPlus: Float,
    val paymentsMinus: Float,
    val summary: Float,
    val upakCompliteDateListStr: List<String>,
    val shptCompliteDateListStr: List<String>
)

data class StatSummaryByDate(
    val currentDateStr: String,
    val upakCount: Int,
    val upakCost: Float,
    val shptCount: Int,
    val shptCost: Float
)