package com.example.entshptapplication.shpt.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.entshptapplication.shpt.dbModels.ShptDoorDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ShptDao {
    @Query("SELECT * FROM shpt_doors  ORDER BY id ASC")
    fun getAll(): Flow<List<ShptDoorDb>>

    @Query("SELECT * FROM shpt_doors WHERE actId=:actId ORDER BY id ASC")
    suspend fun getAll(actId: Int): List<ShptDoorDb>

    @Query("SELECT * FROM shpt_doors WHERE actId=:actId AND num LIKE :filter ORDER BY id ASC")
    fun get(actId: Int, filter: String?): Flow<List<ShptDoorDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actDoorDb: ShptDoorDb)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(actDoorsDb: List<ShptDoorDb>)

    @Query("DELETE FROM shpt_doors WHERE actId=:actId AND naryadId=:naryadId")
    suspend fun delete(actId: Int, naryadId: Int)

    @Query("DELETE FROM shpt_doors WHERE actId=:actId")
    suspend fun clear(actId: Int)
}