package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.finexetf.room.Fund
import io.github.mamedovilkin.finexetf.room.FundDatabase
import javax.inject.Inject

class LocalRepository @Inject constructor(database: FundDatabase) {

    private val dao = database.getDao()

    suspend fun insert(fund: Fund) {
        dao.insert(fund)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    fun getLocalFunds(): LiveData<List<Fund>> {
        return dao.getLocalFunds()
    }
}