package io.github.mamedovilkin.core.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import io.github.mamedovilkin.core.repository.DatabaseRepository
import io.github.mamedovilkin.core.repository.NetworkRepository
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.network.model.cbr.ValCurs
import io.github.mamedovilkin.network.model.finex.Fund
import io.github.mamedovilkin.network.model.finex.ListFund
import retrofit2.Response
import javax.inject.Inject

class MyAssetsUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    databaseRepository: DatabaseRepository
) {

    val funds: LiveData<List<ListFund>> = networkRepository.cachedFunds.asLiveData()
    val assets: LiveData<List<Asset>> = databaseRepository.getAssets()

    suspend fun getCurrencies(dateReq: String): Response<ValCurs> {
        return networkRepository.getCurrencies(dateReq)
    }

    suspend fun getFund(ticker: String): Response<Fund> {
        return networkRepository.getFund(ticker)
    }
}