package com.example.entshptapplication.communications

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitCreator {
    companion object {
        fun getRetrofit(hostedName: String):Retrofit {
            val paramInterceptor = Interceptor{ chain ->
                val url = chain.request().url.toString()

                val response = chain.proceed(chain.request())
                val responseBody = response.peekBody(1024*1024)
                return@Interceptor response
            }

            var client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                //.addInterceptor(paramInterceptor)
                    /*
                .addInterceptor { chain ->
                    val request = chain
                        .request()
                        .newBuilder()
                        .build()
                    var resp: Response
                    try {
                        resp = chain.proceed(request)
                    } catch (e: Exception) {
                        val errorTxt = "Unexpected error"
                        var re = IOException(errorTxt)
                        re.initCause(e)
                        throw re
                    }
                    resp
                }
                */
                .build()

            val retrofit = Retrofit.Builder().baseUrl(hostedName)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return  retrofit;
        }
    }
}