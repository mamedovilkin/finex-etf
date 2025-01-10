package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.network.model.finex.Fund
import io.github.mamedovilkin.network.model.finex.ListFund
import io.github.mamedovilkin.network.model.blog.Posts
import io.github.mamedovilkin.network.model.cbr.ValCurs
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

    suspend fun getPosts(page: Int): Response<Posts> {
        return networkRepository.getPosts(page)
    }
}