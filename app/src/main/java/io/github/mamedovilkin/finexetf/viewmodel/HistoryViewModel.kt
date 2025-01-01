package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.repository.FondUseCase
import io.github.mamedovilkin.finexetf.room.Fond
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    useCase: FondUseCase
) : ViewModel() {

    val fonds: LiveData<List<Fond>> = useCase.getLocalFonds()
}