package com.example.entshptapplication.models

import java.util.Date

data class ActShpt(
    val idAct: Int,
    val actNum: Int,
    val actDate: Date,
    val shptDate: Date?,
    val actDateStr: String?,
    val shptDateStr: String?,
    val shet: String?,
    val orgName: String,
    val fahrer: String,
    val carNum: String,
    var doorCount: Int?,
    var doors: List<ActShptDoor> = listOf()
)

data class ActShptDoor(
    val id: Int,
    val idNaryad: Int,
    val doorId: Int,
    val shet: String,
    val shetDateStr: String,
    val numInOrder: Int,
    val num: String,
    val note: String?,
    val shtild: String?,
    val upakComplite: Boolean,
    val shptComplite: Boolean,
    var isInDb: Boolean = false
)
