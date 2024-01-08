package com.example.entshptapplication.di.dataModules

import com.example.entshptapplication.communications.FindNaryadsApi
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.communications.QueryConverterFactory
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.ui.naryadInfo.api.NaryadInfoApi
import com.example.entshptapplication.communications.StatisticsApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL="http://192.168.1.200:5226/"
    //private const val BASE_URL="http://192.168.0.254:5226/"

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gsonCOnverter = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gsonCOnverter))
            .addConverterFactory(QueryConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providerLoginApi(retrofit: Retrofit):LoginApi = retrofit.create(LoginApi::class.java)

    @Singleton
    @Provides
    fun providerFindNaryadsApi(retrofit: Retrofit):FindNaryadsApi = retrofit.create(FindNaryadsApi::class.java)

    @Singleton
    @Provides
    fun providerShptApi(retrofit: Retrofit): ShptApi = retrofit.create(ShptApi::class.java)

    @Singleton
    @Provides
    fun providerUpakApi(retrofit: Retrofit):UpakApi = retrofit.create(UpakApi::class.java)

    @Singleton
    @Provides
    fun providerNaryadInfoApi(retrofit: Retrofit):NaryadInfoApi = retrofit.create(NaryadInfoApi::class.java)

    @Singleton
    @Provides
    fun providerStatisticsApi(retrofit: Retrofit): StatisticsApi = retrofit.create(StatisticsApi::class.java)
}