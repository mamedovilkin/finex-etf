package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import io.github.mamedovilkin.finexetf.model.database.Asset
import io.github.mamedovilkin.network.model.finex.Fund
import io.github.mamedovilkin.network.model.finex.ListFund
import io.github.mamedovilkin.network.model.blog.Posts
import io.github.mamedovilkin.network.model.cbr.ValCurs
import retrofit2.Response
import javax.inject.Inject

class UseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend fun insert(asset: Asset) {
        repository.insert(asset)
    }

    suspend fun delete(asset: Asset) {
        repository.delete(asset)
    }

    suspend fun deleteAllAssets() {
        repository.deleteAllAssets()
    }

    fun getAssets(): LiveData<List<Asset>> {
        return repository.getAssets()
    }

    suspend fun getFunds(): Response<List<ListFund>> {
        return repository.getFunds()
    }

    suspend fun getFund(ticker: String): Response<Fund> {
        return repository.getFund(ticker)
    }

    suspend fun getCurrencies(dateReq: String): Response<ValCurs> {
        return repository.getCurrencies(dateReq)
    }

    suspend fun getPosts(page: Int): Response<Posts> {
        return repository.getPosts(page)
    }
}