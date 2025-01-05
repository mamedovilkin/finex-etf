package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.repository.UseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    fun deleteAllAssets() {
        viewModelScope.launch {
            useCase.deleteAllAssets()
        }
    }
}