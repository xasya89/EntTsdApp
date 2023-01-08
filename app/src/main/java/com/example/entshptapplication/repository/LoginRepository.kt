package com.example.entshptapplication.repository

import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.models.LoginModel
import com.example.entshptapplication.models.Worker
import retrofit2.Call

class LoginRepository constructor(private val loginApi: LoginApi) {
    fun Authorize(model: LoginModel): Call<Worker> = loginApi.Authorize(model)
}