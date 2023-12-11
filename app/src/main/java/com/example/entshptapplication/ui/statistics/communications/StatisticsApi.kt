package com.example.entshptapplication.ui.statistics.communications

import com.example.entshptapplication.communications.RetrofitCreator
import com.example.entshptapplication.ui.statistics.models.CreateModel
import com.example.entshptapplication.ui.statistics.models.CreateResponseModel
import com.example.entshptapplication.ui.statistics.models.SummaryModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StatisticsApi {
    @POST("api/new-statistics")
    suspend fun addJob(@Body model: CreateModel): CreateResponseModel

    @GET("api/new-statistics")
    suspend fun getStatistic(@Query("uuid") uuid: String):Response<SummaryModel?>

    companion object {
        var api: StatisticsApi? = null

        fun getInstance(hostedName: String): StatisticsApi {
            if(api == null){
                api = RetrofitCreator.getRetrofit(hostedName).create(StatisticsApi::class.java)
            }
            return api!!
        }
    }
}