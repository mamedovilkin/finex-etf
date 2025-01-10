package io.github.mamedovilkin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.lifecycle.LiveData
import io.github.mamedovilkin.database.entity.Asset

@Dao
interface AssetDao {

    @Insert
    suspend fun insert(asset: Asset)

    @Delete
    suspend fun delete(asset: Asset)

    @Query("DELETE FROM assets")
    suspend fun deleteAllAssets()

    @Query("SELECT * FROM assets ORDER BY ticker DESC")
    fun getAssets(): LiveData<List<Asset>>
}