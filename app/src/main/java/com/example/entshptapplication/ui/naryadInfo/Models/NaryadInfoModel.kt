package com.example.entshptapplication.ui.naryadInfo.Models

data class NaryadInfoModel(
    val shet: String,
    val shetDateStr: String,
    val numInOrder: Int,
    val naryadNum: String?,
    val customer: String,
    val doorName: String,
    val doorSize: String,
    val open: String,
    val ral: String,
    val dovod: String,
    val nalichnik: String,
    val shtild: String?,
    val note: String,

    val svarkaFio: String?,
    val svarkaDateCompliteStr: String?,
    val sborkaFio: String?,
    val sborkaDateCompliteStr: String?,
    val colorFio: String?,
    val colorDateCompliteStr: String?,
    val upakFio: String?,
    val upakDateCompliteStr: String?,
    val shptFio: String?,
    val shptDateCompliteStr: String?
)
