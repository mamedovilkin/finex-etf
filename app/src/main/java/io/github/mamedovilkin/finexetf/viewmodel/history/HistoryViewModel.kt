package io.github.mamedovilkin.finexetf.viewmodel.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.finexetf.model.database.Asset
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    val assets: LiveData<List<Asset>> = useCase.getAssets()

    fun delete(asset: Asset) {
        viewModelScope.launch {
            useCase.delete(asset)
        }
    }
}