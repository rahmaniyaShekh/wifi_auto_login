package com.aipg.wifilogin.localData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface dataDao {

    @Insert
    suspend fun insert(dataClass: dataClass)

    @Delete
    suspend fun delete(dataClass: dataClass)

    @Query(value = "Select * from addStockTable order by id ASC")
    fun getAllDataLive(): LiveData<List<dataClass>>
}