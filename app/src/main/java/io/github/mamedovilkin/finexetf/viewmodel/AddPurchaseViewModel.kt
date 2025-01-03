package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.repository.FondUseCase
import io.github.mamedovilkin.finexetf.room.Fond
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPurchaseViewModel @Inject constructor(
    private val useCase: FondUseCase
) : ViewModel() {

    fun insert(fond: Fond) {
        CoroutineScope(Dispatchers.IO).launch {
            useCase.insert(fond)
        }
    }
}