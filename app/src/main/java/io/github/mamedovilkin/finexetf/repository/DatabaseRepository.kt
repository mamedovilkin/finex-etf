package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.finexetf.model.database.Asset
import io.github.mamedovilkin.finexetf.database.AssetDatabase
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