package com.example.entshptapplication.communications

import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.LoginModel
import com.example.entshptapplication.models.Worker
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("api/login")
    fun Authorize(@Body loginModel: LoginModel): Call<Worker>

    companion object {
        var loginApi: LoginApi? = null
        fun getInstance(): LoginApi {
            if(loginApi == null){
                val retrofit = Retrofit.Builder().baseUrl(HOSTED_NAME)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                loginApi = retrofit.create(LoginApi::class.java)
            }
            return loginApi!!
        }

        fun getInstance(hostedName: String): LoginApi {
            if(loginApi == null){
                loginApi = RetrofitCreator.getRetrofit(hostedName).create(LoginApi::class.java)
            }
            return loginApi!!
        }
    }
}