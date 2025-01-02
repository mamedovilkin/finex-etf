package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.finexetf.room.Fund
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    useCase: UseCase
) : ViewModel() {

    val funds: LiveData<List<Fund>> = useCase.getLocalFunds()
}