package com.example.entshptapplication.communications

import com.example.entshptapplication.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ShptApi {
    @GET("api/v2/actshpt")
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
    suspend fun Complite(@Body model: ShptCompliteListRequestPayload): Response<List<ActShptDoor>>

    @HTTP(method = "DELETE", path = "api/actshpt", hasBody = true)
    suspend fun Delete(@Body model: ShptCompliteDeleteRequestPayload): ResponseBody

}