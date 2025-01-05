package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.finexetf.model.database.Asset
import io.github.mamedovilkin.finexetf.model.network.Fund
import io.github.mamedovilkin.finexetf.model.network.ListFund
import io.github.mamedovilkin.finexetf.model.network.ValCurs
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository,
) {

    suspend fun insert(asset: Asset) {
        databaseRepository.insert(asset)
    }

    suspend fun delete(asset: Asset) {
        databaseRepository.delete(asset)
    }

    suspend fun deleteAllAssets() {
        databaseRepository.deleteAllAssets()
    }

    fun getAssets(): LiveData<List<Asset>> {
        return databaseRepository.getAssets()
    }

    suspend fun getFunds(): Response<List<ListFund>> {
        return networkRepository.getFunds()
    }

    suspend fun getFund(ticker: String): Response<Fund> {
        return networkRepository.getFund(ticker)
    }

    suspend fun getCurrencies(dateReq: String): Response<ValCurs> {
        return networkRepository.getCurrencies(dateReq)
    }
}