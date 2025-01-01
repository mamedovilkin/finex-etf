package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.finexetf.model.Fond
import io.github.mamedovilkin.finexetf.model.Fonds
import retrofit2.Response
import javax.inject.Inject

class FondUseCase @Inject constructor(
    private val repository: FondRepository,
) {

    suspend fun insert(fond: io.github.mamedovilkin.finexetf.room.Fond) {
        repository.insert(fond)
    }

    suspend fun deleteAll() {
        repository.deleteAll()
    }

    fun getLocalFonds(): LiveData<List<io.github.mamedovilkin.finexetf.room.Fond>> {
        return repository.getLocalFonds()
    }

    suspend fun getFonds(): Response<Fonds> {
        return repository.getFonds()
    }

    suspend fun getFondByTicker(ticker: String): Response<Fond> {
        return repository.getFondByTicker(ticker)
    }

}