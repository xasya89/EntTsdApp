package com.example.entshptapplication.models

import java.util.Date

data class StatSummary(
    val dateWithStr:String,
    val balancePrevSalary: Float,
    val costUpak: Float,
    val costShpt: Float,
    val paymentPlus: Float,
    val paymentMinus: Float,
    val summary: Float
)
