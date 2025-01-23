package io.github.mamedovilkin.finexetf.view.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.core.usecase.HistoryUseCase
import io.github.mamedovilkin.database.entity.Asset
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCase: HistoryUseCase
) : ViewModel() {

    val assets: LiveData<List<Asset>> = historyUseCase.assets

    fun delete(asset: Asset) {
        viewModelScope.launch {
            historyUseCase.delete(asset)
        }
    }
}