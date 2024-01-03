package com.example.entshptapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.entshptapplication.databaseModels.LoginDb
import org.jetbrains.annotations.NotNull
import java.time.LocalDate
import java.util.Date

@Dao
@TypeConverters(Converters::class)
interface LoginDao {
    @Query("SELECT * FROM Logins WHERE LoginDate=:loginDate AND isLogout=0 LIMIT 1")
    suspend fun GetLogin(loginDate: LocalDate): LoginDb?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun Insert(loginDb: LoginDb)

    @Query("UPDATE Logins SET isLogout=1")
    suspend fun Logout()
}