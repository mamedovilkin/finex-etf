package io.github.mamedovilkin.finexetf.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser
import io.github.mamedovilkin.database.entity.Asset
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

    val funds: LiveData<List<ListFund>> = repository.funds

    suspend fun getFund(ticker: String): Response<Fund> {
        return repository.getFund(ticker)
    }

    suspend fun getCurrencies(dateReq: String): Response<ValCurs> {
        return repository.getCurrencies(dateReq)
    }

    suspend fun getPosts(page: Int): Response<Posts> {
        return repository.getPosts(page)
    }

    suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
        return repository.signInWithGoogle(idToken)
    }

    fun getCurrentUser(): FirebaseUser? = repository.getCurrentUser()

    fun signOut() = repository.signOut()

    fun backupAsset(uid: String, asset: io.github.mamedovilkin.core.model.Asset) {
        repository.backupAsset(uid, asset)
    }

    fun getBackup(uid: String): LiveData<List<io.github.mamedovilkin.core.model.Asset>> {
        return repository.getBackup(uid)
    }
}