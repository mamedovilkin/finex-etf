package io.github.mamedovilkin.database.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.database.database.AssetDatabase
import io.github.mamedovilkin.database.entity.Asset
import javax.inject.Inject

class DatabaseRepository @Inject constructor(database: AssetDatabase) {

    private val dao = database.getDao()

    suspend fun insert(asset: Asset) {
        dao.insert(asset)
    }

    suspend fun delete(asset: Asset) {
        dao.delete(asset)
    }

    suspend fun deleteAllAssets() {
        dao.deleteAllAssets()
    }

    fun getAssets(): LiveData<List<Asset>> {
        return dao.getAssets()
    }
}