package io.github.mamedovilkin.finexetf.view.fragment.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.core.usecase.SettingsUseCase
import io.github.mamedovilkin.database.entity.Asset
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val assets: LiveData<List<Asset>> = settingsUseCase.assets

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = settingsUseCase.signInWithGoogle(idToken)
            if (result != null) {
                _user.value = result
            } else {
                _error.value = "Authentication failed"
            }
        }
    }

    fun getCurrentUser(): LiveData<FirebaseUser?> {
        return liveData { emit(settingsUseCase.getCurrentUser()) }
    }

    fun signOut() {
        settingsUseCase.signOut()
        _user.value = null
    }

    fun deleteAllAssets() {
        viewModelScope.launch {
            settingsUseCase.deleteAllAssets()
        }
    }

    fun backupAssets(uid: String, assets: List<Asset>) {
        viewModelScope.launch {
            settingsUseCase.backupAsset(uid, assets)
        }
    }

    fun getBackup(uid: String) {
        viewModelScope.launch {
            settingsUseCase.getBackup(uid)
        }
    }
}