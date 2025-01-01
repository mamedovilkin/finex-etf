package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.finexetf.model.Fond
import io.github.mamedovilkin.finexetf.model.Fonds
import retrofit2.Response
import javax.inject.Inject

class FondRepository @Inject constructor(
    private val remoteRepository: FondRemoteRepository,
    private val localRepository: FondLocalRepository,
) {

    suspend fun insert(fond: io.github.mamedovilkin.finexetf.room.Fond) {
        localRepository.insert(fond)
    }

    suspend fun deleteAll() {
        localRepository.deleteAll()
    }

    fun getLocalFonds(): LiveData<List<io.github.mamedovilkin.finexetf.room.Fond>> {
        return localRepository.getLocalFonds()
    }

    suspend fun getFonds(): Response<Fonds> {
        return remoteRepository.getFonds()
    }

    suspend fun getFondByTicker(ticker: String): Response<Fond> {
        return remoteRepository.getFondByTicker(ticker)
    }

}