package com.example.entshptapplication.ui.statistics.models

import java.util.Date

data class SummaryModel(
    val workerId: Int,
    val lastActSum: Double,
    val paymentsSumAll: Double,
    val upakSumAll: Double,
    val shptSumAll: Double,
    val paymentDays: List<SummayDayPayment>,
    val upakDays: List<SummaryDayNaryadComplite>,
    val shptDays: List<SummaryDayNaryadComplite>
)

data class SummayDayPayment(
    val day: Date,
    val sum: Double
)

data class SummaryDayNaryadComplite(
    val day: Date,
    val compliteSum: Double,
    val compliteCount: Int
)