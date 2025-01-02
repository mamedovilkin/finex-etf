package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.repository.UseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            useCase.deleteAll()
        }
    }
}