package com.example.entshptapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.entshptapplication.databaseModels.ShptDoorDb
import com.example.entshptapplication.databaseModels.UpakNaryadDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ShptDao {
    @Query("SELECT * FROM shpt_doors  ORDER BY id ASC")
    fun getAll(): Flow<List<ShptDoorDb>>

    @Query("SELECT * FROM shpt_doors WHERE actId=:actId ORDER BY id ASC")
    fun getAll(actId: Int): Flow<List<ShptDoorDb>>

    @Query("SELECT * FROM shpt_doors WHERE actId=:actId AND num LIKE :filter ORDER BY id ASC")
    fun get(actId: Int, filter: String?): Flow<List<ShptDoorDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actDoorDb: ShptDoorDb)

    @Query("DELETE FROM shpt_doors WHERE actId=:actId AND naryadId=:naryadId")
    suspend fun delete(actId: Int, naryadId: Int)

    @Query("DELETE FROM shpt_doors WHERE actId=:actId")
    suspend fun clear(actId: Int)
}