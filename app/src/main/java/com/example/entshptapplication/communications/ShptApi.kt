package com.example.entshptapplication.communications

import com.example.entshptapplication.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShptApi {
    @GET("api/actshpt")
    fun GetActList():Call<List<ActShpt>>

    @GET("api/actshpt/{idAct}")
    fun GetOne(@Path("idAct") idAct:Int, @Query("find") find:String): Call<ActShpt>

    @POST("api/actshpt/scanlist")
    fun ScanList(@Body model: ShptCompliteListRequestPayload): Call<List<ActShptDoor>>

    @POST("api/actshpt/scan")
    fun Scan(@Body model: ShptCompliteRequestPayload): Call<ActShptDoor>

    @HTTP(method = "DELETE", path = "api/actshpt", hasBody = true)
    fun Delete(@Body model: ShptCompliteDeleteRequestPayload): Call<ResponseBody>

    companion object {
        var shptApi: ShptApi? = null

        fun getInstance(hostedName: String): ShptApi {
            if(shptApi == null){
                val retrofit = Retrofit.Builder().baseUrl(hostedName)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                shptApi = retrofit.create(ShptApi::class.java)
            }
            return shptApi!!
        }
    }
}