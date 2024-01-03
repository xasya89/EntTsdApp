package com.example.entshptapplication.communications

import com.example.entshptapplication.models.FindNaryadModel
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.Naryad
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FindNaryadsApi {
    @GET("api/findNaryad")
    suspend fun Find(@Query("find") findStr:String): List<FindNaryadModel>

    @GET("api/findNaryad/{naryadId}")
    suspend fun GetNaryad(@Path("naryadId") naryadId: Int): FindNaryadModel
}