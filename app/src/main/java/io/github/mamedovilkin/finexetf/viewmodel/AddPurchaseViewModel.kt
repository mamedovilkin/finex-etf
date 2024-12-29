package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.model.Fond
import io.github.mamedovilkin.finexetf.repository.FondUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPurchaseViewModel @Inject constructor(
    private val useCase: FondUseCase
) : ViewModel() {

    fun insert(fond: io.github.mamedovilkin.finexetf.room.Fond) {
        CoroutineScope(Dispatchers.IO).launch {
            useCase.insert(fond)
        }
    }

    fun getFondByTicker(ticker: String): LiveData<Fond> {
        return liveData {
            useCase.getFondByTicker(ticker).body()?.let {
                emit(it)
            }
        }
    }

}