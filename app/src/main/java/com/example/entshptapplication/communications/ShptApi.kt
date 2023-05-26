package com.example.entshptapplication.communications

import com.example.entshptapplication.models.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


interface ShptApi {
    @GET("api/actshpt")
    suspend fun GetActList():List<ActShpt>

    @GET("api/actshpt/{idAct}")
    suspend fun GetOne(@Path("idAct") idAct:Int, @Query("find") find:String): ActShpt

    @GET("api/actshpt/one")
    fun getNaryadOne(@Query("workerId") workerId: Int, @Query("naryadId") naryadId: Int): Call<ActShptDoor>

    @GET("api/actshpt/list")
    suspend fun getNaryadList(@Query("workerId") workerId: Int, @Query("naryadIdListStr") naryadIdListStr:String): List<ActShptDoor>

    @GET("api/actshpt/scan")
    suspend fun Scan(@Query("workerId") workerId: Int, @Query("barcode") barcode:String): ActShptDoor

    @POST("api/actshpt")
    suspend fun Complite(@Body model: ShptCompliteListRequestPayload): List<ActShptDoor>

    @HTTP(method = "DELETE", path = "api/actshpt", hasBody = true)
    suspend fun Delete(@Body model: ShptCompliteDeleteRequestPayload): ResponseBody

    companion object {
        var shptApi: ShptApi? = null

        fun getInstance(hostedName: String): ShptApi {
            if(shptApi == null){
                shptApi = RetrofitCreator.getRetrofit(hostedName).create(ShptApi::class.java)
            }
            return shptApi!!
        }
    }
}