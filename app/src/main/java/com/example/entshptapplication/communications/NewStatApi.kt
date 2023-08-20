package com.example.entshptapplication.communications

import com.example.entshptapplication.models.StatNaryad
import com.example.entshptapplication.models.StatSummary
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewStatApi {
    @GET("api/new-stat/summary/{workerId}")
    suspend fun getSummary(@Path("workerId") workerId: Int, @Query("selectDate") selectDate: String?): StatSummary

    @GET("api/new-stat/details/{workerId}")
    suspend fun getDetail(@Path("workerId") workerId: Int, @Query("step") step:Int, @Query("date") date:String?, @Query("start") start: Int, @Query("count") count: Int): List<StatNaryad>

    @DELETE("api/new-stat/upak/{naryadId}")
    suspend fun deleteUpakNaryad(@Path("naryadId") naryadId:Int, @Query("workerId") workerId: Int)

    @DELETE("api/new-stat/shpt/{naryadId}")
    suspend fun deleteShptNaryad(@Path("naryadId") naryadId:Int, @Query("workerId") workerId: Int)

    companion object {
        var newApi: NewStatApi? = null

        fun getInstance(hostedName: String): NewStatApi {
            if(newApi == null){
                newApi = RetrofitCreator.getRetrofit(hostedName).create(NewStatApi::class.java)
            }
            return newApi!!
        }
    }
}