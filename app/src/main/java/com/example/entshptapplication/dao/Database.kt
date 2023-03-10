package com.example.entshptapplication.dao

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.entshptapplication.databaseModels.ShptDoorDb
import com.example.entshptapplication.databaseModels.UpakNaryadDb
import com.example.entshptapplication.models.ConnectionSetting


@androidx.room.Database(entities = arrayOf(ConnectionSetting::class, UpakNaryadDb::class, ShptDoorDb::class), version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun upakDao(): UpakDao
    abstract fun shptDao(): ShptDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ent-shpt"
                )
                    .addMigrations(MIGRATION_ADD_UPAKNARYADS)
                    .addMigrations(MIGRATION_ADD_SHPT)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

val MIGRATION_ADD_UPAKNARYADS: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Поскольку мы не изменяли таблицу, здесь больше нечего делать.
    }
}

val MIGRATION_ADD_SHPT: Migration = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Поскольку мы не изменяли таблицу, здесь больше нечего делать.
    }
}