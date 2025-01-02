package io.github.mamedovilkin.finexetf.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FundDao {

    @Insert
    suspend fun insert(fund: Fund)

    @Query("DELETE FROM assets")
    suspend fun deleteAll()

    @Query("SELECT * FROM assets ORDER BY ticker DESC")
    fun getLocalFunds(): LiveData<List<Fund>>
}