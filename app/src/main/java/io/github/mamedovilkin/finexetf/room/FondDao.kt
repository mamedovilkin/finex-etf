package io.github.mamedovilkin.finexetf.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FondDao {

    @Insert
    suspend fun insert(fond: Fond)

    @Delete
    suspend fun delete(fond: Fond)

    @Query("SELECT * FROM assets ORDER BY ticker DESC")
    fun getLocalFonds(): LiveData<List<Fond>>
}