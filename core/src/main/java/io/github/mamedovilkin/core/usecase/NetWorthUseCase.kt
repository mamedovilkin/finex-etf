package io.github.mamedovilkin.core.usecase

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.core.repository.DatabaseRepository
import io.github.mamedovilkin.core.repository.NetworkRepository
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.network.model.cbr.ValCurs
import io.github.mamedovilkin.network.model.finex.Fund
import retrofit2.Response
import javax.inject.Inject

class NetWorthUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    databaseRepository: DatabaseRepository
) {
    val assets: LiveData<List<Asset>> = databaseRepository.getAssets()

    suspend fun getCurrencies(dateReq: String): Response<ValCurs> {
        return networkRepository.getCurrencies(dateReq)
    }

    suspend fun getFund(ticker: String): Response<Fund> {
        return networkRepository.getFund(ticker)
    }
}