package com.example.entshptapplication.NaryadInfo.api

import com.example.entshptapplication.NaryadInfo.Models.NaryadInfoModel
import com.example.entshptapplication.communications.RetrofitCreator
import retrofit2.http.GET
import retrofit2.http.Query

interface NaryadInfoApi {
    @GET("api/naryad-info/getByNaryadNum")
    suspend fun Get(@Query("findStr") findStr: String): NaryadInfoModel?

    @GET("api/naryad-info/getByNaryadId")
    suspend fun GetById(@Query("naryadId") naryadId: String): NaryadInfoModel?

    companion object {
        var naryadInfoApi: NaryadInfoApi? = null

        fun getInstance(hostedName: String): NaryadInfoApi {
            if(naryadInfoApi == null){
                naryadInfoApi = RetrofitCreator.getRetrofit(hostedName).create(NaryadInfoApi::class.java)
            }
            return naryadInfoApi!!
        }
    }
}