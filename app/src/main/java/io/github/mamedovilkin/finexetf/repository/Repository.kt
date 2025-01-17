package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseUser
import io.github.mamedovilkin.core.repository.CoreRepository
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.database.repository.DatabaseRepository
import io.github.mamedovilkin.network.model.finex.Fund
import io.github.mamedovilkin.network.model.finex.ListFund
import io.github.mamedovilkin.network.model.blog.Posts
import io.github.mamedovilkin.network.model.cbr.ValCurs
import io.github.mamedovilkin.network.repository.NetworkRepository
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository,
    private val coreRepository: CoreRepository,
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

    val funds: LiveData<List<ListFund>> = networkRepository.cachedFunds.asLiveData()

    suspend fun getFund(ticker: String): Response<Fund> {
        return networkRepository.getFund(ticker)
    }

    suspend fun getCurrencies(dateReq: String): Response<ValCurs> {
        return networkRepository.getCurrencies(dateReq)
    }

    suspend fun getPosts(page: Int): Response<Posts> {
        return networkRepository.getPosts(page)
    }

    suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
        return coreRepository.signInWithGoogle(idToken)
    }

    fun getCurrentUser(): FirebaseUser? = coreRepository.getCurrentUser()

    fun signOut() = coreRepository.signOut()

    fun backupAsset(uid: String, assets: List<Asset>) {
        coreRepository.backupAsset(uid, assets)
    }

    fun getBackup(uid: String): LiveData<List<io.github.mamedovilkin.core.model.Asset>> {
        return coreRepository.getBackup(uid)
    }
}