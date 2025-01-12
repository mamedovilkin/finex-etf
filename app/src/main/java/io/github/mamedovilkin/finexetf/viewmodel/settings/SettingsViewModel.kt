package io.github.mamedovilkin.finexetf.viewmodel.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.finexetf.repository.UseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val assets: LiveData<List<Asset>> = useCase.getAssets()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = useCase.signInWithGoogle(idToken)
            if (result != null) {
                _user.value = result
            } else {
                _error.value = "Authentication failed"
            }
        }
    }

    fun getCurrentUser(): LiveData<FirebaseUser?> {
        return liveData { emit(useCase.getCurrentUser()) }
    }

    fun signOut() {
        useCase.signOut()
        _user.value = null
    }

    fun deleteAllAssets() {
        viewModelScope.launch {
            useCase.deleteAllAssets()
        }
    }

    fun backupAssets(uid: String, assets: List<Asset>) {
        viewModelScope.launch {
            assets.forEach {
                val asset = io.github.mamedovilkin.core.model.Asset(
                    it.id,
                    it.ticker,
                    it.icon,
                    it.name,
                    it.originalName,
                    it.isActive,
                    it.navPrice,
                    it.currencyNav,
                    it.quantity,
                    it.datetime,
                    it.price,
                    it.type,
                )
                useCase.backupAsset(uid, asset)
            }
        }
    }

    fun getBackup(uid: String) {
        viewModelScope.launch {
            useCase.getBackup(uid).asFlow().collect {
                it.forEach {
                    val asset = Asset(it.id ?: "", it.ticker ?: "", it.icon ?: "", it.name ?: "", it.originalName ?: "", it.isActive ?: false, it.navPrice ?: 0.0, it.currencyNav ?: "", it.quantity ?: 0, it.datetime ?: 0L, it.price ?: 0.0, it.type ?: Converter.fromType(Type.PURCHASE),)
                    useCase.insert(asset)
                }
            }
        }
    }
}