package com.example.entshptapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.entshptapplication.databaseModels.UpakNaryadDb
import kotlinx.coroutines.flow.Flow

@Dao
interface UpakDao {
    @Query("SELECT * FROM upak_naryads ORDER BY id ASC")
    fun getAll():Flow<List<UpakNaryadDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(naryadDb: UpakNaryadDb)

    @Query("DELETE FROM upak_naryads")
    suspend fun clear()
}