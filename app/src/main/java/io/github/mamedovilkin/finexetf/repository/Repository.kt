package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.finexetf.model.Fund
import io.github.mamedovilkin.finexetf.model.Funds
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) {

    suspend fun insert(fund: io.github.mamedovilkin.finexetf.room.Fund) {
        localRepository.insert(fund)
    }

    suspend fun deleteAll() {
        localRepository.deleteAll()
    }

    fun getLocalFunds(): LiveData<List<io.github.mamedovilkin.finexetf.room.Fund>> {
        return localRepository.getLocalFunds()
    }

    suspend fun getFunds(): Response<Funds> {
        return remoteRepository.getFunds()
    }

    suspend fun getFundByTicker(ticker: String): Response<Fund> {
        return remoteRepository.getFundByTicker(ticker)
    }

}