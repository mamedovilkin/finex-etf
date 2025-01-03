package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.model.Fonds
import io.github.mamedovilkin.finexetf.repository.FondUseCase
import io.github.mamedovilkin.finexetf.room.Fond
import javax.inject.Inject

@HiltViewModel
class MyAssetsViewModel @Inject constructor(
    private val useCase: FondUseCase
) : ViewModel() {

    val remoteFonds: LiveData<Fonds> = liveData { useCase.getFonds().body()?.let { emit(it) } }
    val localFonds: LiveData<List<Fond>> = useCase.getLocalFonds()
}