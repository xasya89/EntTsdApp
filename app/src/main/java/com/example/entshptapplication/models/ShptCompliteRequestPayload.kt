package com.example.entshptapplication.models

data class ShptCompliteRequestPayload(
    val actId: Int,
    val barCode: String,
    val naryadId: Int,
    val workerId: Int
)
data class ShptCompliteListRequestPayload(
    val actId: Int,
    val barCode: String = "",
    val naryadsId: List<Int>,
    val workerId: Int
)

data class ShptCompliteDeleteRequestPayload(
    val actDoorId: Int,
    val workerId: Int
)