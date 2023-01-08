package com.example.entshptapplication.communications

import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.models.RequestUpakModel
import com.example.entshptapplication.models.ResponseMessageModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UpakApi {
    @POST("api/upaklist")
    fun Scan(@Body barcode: String): Call<Naryad>

    @POST("api/upaklist/save")
    fun Save(@Body model: RequestUpakModel): Call<ResponseMessageModel>

    companion object {
        var upakApi: UpakApi? = null

        fun getInstance(hostedName: String): UpakApi {
            if(upakApi == null){
                val retrofit = Retrofit.Builder().baseUrl(hostedName)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                upakApi = retrofit.create(UpakApi::class.java)
            }
            return upakApi!!
        }
    }
}