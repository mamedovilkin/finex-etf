package io.github.mamedovilkin.core.usecase

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser
import io.github.mamedovilkin.core.repository.DatabaseRepository
import io.github.mamedovilkin.core.repository.FirebaseRepository
import io.github.mamedovilkin.database.entity.Asset
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val databaseRepository: DatabaseRepository
) {
    val assets: LiveData<List<Asset>> = databaseRepository.getAssets()

    suspend fun deleteAllAssets() {
        databaseRepository.deleteAllAssets()
    }

    suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
        return firebaseRepository.signInWithGoogle(idToken)
    }

    fun getCurrentUser(): FirebaseUser? = firebaseRepository.getCurrentUser()

    fun signOut() = firebaseRepository.signOut()

    fun backupAsset(uid: String, assets: List<Asset>) {
        firebaseRepository.backupAsset(uid, assets)
    }

    fun getBackup(uid: String) {
        return firebaseRepository.getBackup(uid)
    }
}