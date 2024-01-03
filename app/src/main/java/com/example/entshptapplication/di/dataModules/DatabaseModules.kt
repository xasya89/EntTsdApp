package com.example.entshptapplication.di.dataModules

import android.content.Context
import androidx.room.Room
import com.example.entshptapplication.dao.AppDatabase
import com.example.entshptapplication.dao.MIGRATION_ADD_LOGIN
import com.example.entshptapplication.dao.MIGRATION_ADD_SHPT
import com.example.entshptapplication.dao.MIGRATION_ADD_UPAKNARYADS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModules {
    @Singleton
    @Provides
    fun providerDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "ent-shpt"
    )
        .addMigrations(MIGRATION_ADD_UPAKNARYADS)
        .addMigrations(MIGRATION_ADD_SHPT)
        .addMigrations(MIGRATION_ADD_LOGIN)
        .build()

    @Singleton
    @Provides
    fun providerLoginDao(db: AppDatabase) = db.loginDao()
    @Singleton
    @Provides
    fun providerSettingsDao(db: AppDatabase) = db.settingsDao()
    @Singleton
    @Provides
    fun providerShptDao(db: AppDatabase) = db.shptDao()
    @Singleton
    @Provides
    fun providerUpakDao(db: AppDatabase) = db.upakDao()
}