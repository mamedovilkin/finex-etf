package io.github.mamedovilkin.core.usecase

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.core.repository.DatabaseRepository
import io.github.mamedovilkin.database.entity.Asset
import javax.inject.Inject

class HistoryUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {

    val assets: LiveData<List<Asset>> = databaseRepository.getAssets()

    suspend fun delete(asset: Asset) {
        databaseRepository.delete(asset)
    }
}