package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.finexetf.model.Fund
import io.github.mamedovilkin.finexetf.model.Funds
import retrofit2.Response
import javax.inject.Inject

class UseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend fun insert(fund: io.github.mamedovilkin.finexetf.room.Fund) {
        repository.insert(fund)
    }

    suspend fun deleteAll() {
        repository.deleteAll()
    }

    fun getLocalFunds(): LiveData<List<io.github.mamedovilkin.finexetf.room.Fund>> {
        return repository.getLocalFunds()
    }

    suspend fun getFunds(): Response<Funds> {
        return repository.getFunds()
    }

    suspend fun getFundByTicker(ticker: String): Response<Fund> {
        return repository.getFundByTicker(ticker)
    }

}