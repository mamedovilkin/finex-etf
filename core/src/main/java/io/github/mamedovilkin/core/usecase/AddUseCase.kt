package io.github.mamedovilkin.core.usecase

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.core.repository.DatabaseRepository
import io.github.mamedovilkin.core.repository.NetworkRepository
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.network.model.finex.Fund
import retrofit2.Response
import javax.inject.Inject

class AddUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository,
) {

    val assets: LiveData<List<Asset>> = databaseRepository.getAssets()

    suspend fun insert(asset: Asset) {
        databaseRepository.insert(asset)
    }

    suspend fun getFund(ticker: String): Response<Fund> {
        return networkRepository.getFund(ticker)
    }
}