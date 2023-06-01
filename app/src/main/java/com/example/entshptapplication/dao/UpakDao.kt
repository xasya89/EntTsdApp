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
    suspend fun getAll():List<UpakNaryadDb>

    @Query("SELECT * FROM upak_naryads WHERE num LIKE :filter ORDER BY id ASC")
    suspend fun get(filter: String?):List<UpakNaryadDb>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(naryadDb: UpakNaryadDb)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(naryadsDb: List<UpakNaryadDb>)

    @Query("DELETE FROM upak_naryads WHERE naryadId=:naryadId")
    suspend fun delete(naryadId: Int)

    @Query("DELETE FROM upak_naryads")
    suspend fun clear()
}