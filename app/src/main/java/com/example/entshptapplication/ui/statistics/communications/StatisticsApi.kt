package com.example.entshptapplication.ui.statistics.communications

import com.example.entshptapplication.communications.RetrofitCreator
import com.example.entshptapplication.ui.statistics.models.CreateModel
import com.example.entshptapplication.ui.statistics.models.CreateResponseModel
import com.example.entshptapplication.ui.statistics.models.ResultStatisticsResponseModel
import com.example.entshptapplication.ui.statistics.models.SummaryModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface StatisticsApi {
    @POST("api/new-statistics")
    suspend fun addJob(@Body model: CreateModel): CreateResponseModel

    @GET("api/new-statistics")
    suspend fun getStatistic(@Query("uuid") uuid: String):Response<SummaryModel?>

    @GET("api/new-statistics/naryads")
    suspend fun getNaryads(@Query("workerId") workerId: Int, @Query("startDate") startDate: Date, @Query("date") date: Date?, @Query("skip") skip:Int?, @Query("take") take: Int?): Response<ResultStatisticsResponseModel>

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