package com.example.entshptapplication.di.dataModules

import com.example.entshptapplication.dao.UpakDao
import com.example.entshptapplication.ui.shpt.ShptDao
import com.example.entshptapplication.ui.shpt.shpt_one.ShptDbRepository1
import com.example.entshptapplication.ui.upak.UpakDbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbRepositoryModules {
    @Singleton
    @Provides
    fun providerShptDbRepository(shptDao: ShptDao)= ShptDbRepository1(shptDao)
    @Singleton
    @Provides
    fun providerUpakDbRepository(upakDao: UpakDao)= UpakDbRepository(upakDao)

}