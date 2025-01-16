package io.github.mamedovilkin.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.mamedovilkin.network.model.finex.ListFund

@Dao
interface FundDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFunds(funds: List<ListFund>)

    @Query("DELETE FROM funds")
    suspend fun deleteAllFunds()

    @Query("SELECT * FROM funds")
    suspend fun getAllFunds(): List<ListFund>
}