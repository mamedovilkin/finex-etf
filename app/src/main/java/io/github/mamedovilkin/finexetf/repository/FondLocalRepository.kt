package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.finexetf.room.Fond
import io.github.mamedovilkin.finexetf.room.FondDao
import io.github.mamedovilkin.finexetf.room.FondDatabase
import javax.inject.Inject

class FondLocalRepository @Inject constructor(database: FondDatabase) {

    private val dao = database.getDao()

    suspend fun insert(fond: Fond) {
        dao.insert(fond)
    }

    suspend fun delete(fond: Fond) {
        dao.delete(fond)
    }

    fun getLocalFonds(): LiveData<List<Fond>> {
        return dao.getLocalFonds()
    }
}