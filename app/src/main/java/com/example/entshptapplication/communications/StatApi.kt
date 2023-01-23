package com.example.entshptapplication.communications

import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.models.StatNaryad
import com.example.entshptapplication.models.StatSummary
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StatApi {
    @GET("api/stat/summary/{workerId}")
    fun getSummary(@Path("workerId") workerId: Int):Call<StatSummary>

    @GET("api/stat/upak/{workerId}")
    fun getUpakNaryads(@Path("workerId") workerId: Int, @Query("find") find: String?, @Query("selectedDateStr") selectedDateStr:String?, @Query("startPos") startPos: Int=0, @Query("stopPos") stopPos: Int = 50): Call<List<StatNaryad>>

    @GET("api/stat/shpt/{workerId}")
    fun getShptNaryads(@Path("workerId") workerId: Int, @Query("find") find: String?, @Query("selectedDateStr") selectedDateStr: String?, @Query("startPos") startPos: Int=0, @Query("stopPos") stopPos: Int = 50): Call<List<StatNaryad>>

    @DELETE("api/stat/upak/{naryadId}")
    fun deleteUpakNaryad(@Path("naryadId") naryadId:Int): Call<ResponseBody>

    companion object {
        var satApi: StatApi? = null

        fun getInstance(hostedName: String): StatApi {
            if(satApi == null){
                val retrofit = Retrofit.Builder().baseUrl(hostedName)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                satApi = retrofit.create(StatApi::class.java)
            }
            return satApi!!
        }
    }
}