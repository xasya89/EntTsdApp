package com.example.entshptapplication.upak.api

import com.example.entshptapplication.communications.RetrofitCreator
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.upak.models.RequestUpakModel
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UpakApi {
    @GET("api/upaklist")
    suspend fun Scan(@Query("barcode") barcode: String): Naryad

    @POST("api/upaklist/save")
    suspend fun Save(@Body model: RequestUpakModel): ResponseBody

    companion object {
        var upakApi: UpakApi? = null

        fun getInstance(hostedName: String): UpakApi {
            if(upakApi == null){
                upakApi = RetrofitCreator.getRetrofit(hostedName).create(UpakApi::class.java)
            }
            return upakApi!!
        }
    }
}