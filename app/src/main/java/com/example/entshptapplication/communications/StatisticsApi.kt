package com.example.entshptapplication.communications

import com.example.entshptapplication.ui.statistics.models.CreateModel
import com.example.entshptapplication.ui.statistics.models.CreateResponseModel
import com.example.entshptapplication.ui.statistics.models.ResultStatisticsResponseModel
import com.example.entshptapplication.ui.statistics.models.SummaryModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    suspend fun getNaryadsWithoutSelectDate(@Query("workerId") workerId: Int, @Query("step") step: Int, @Query("skip") skip:Int?, @Query("take") take: Int?): ResultStatisticsResponseModel

    @GET("api/new-statistics/naryads-date")
    suspend fun getNaryads(@Query("workerId") workerId: Int, @Query("step") step: Int, @Query("date") date: Date, @Query("skip") skip:Int?, @Query("take") take: Int?): ResultStatisticsResponseModel

    @DELETE("api/new-statistics/naryads")
    suspend fun deleteNaryadComplite(@Query("workerId") workerId:Int, @Query("naryadCompliteId") naryadCompliteId: Int)
}