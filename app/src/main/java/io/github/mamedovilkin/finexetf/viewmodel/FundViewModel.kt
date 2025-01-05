package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.model.network.Fund
import io.github.mamedovilkin.finexetf.repository.UseCase
import javax.inject.Inject

@HiltViewModel
class FundViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    fun getFund(ticker: String): LiveData<Fund> {
        return liveData {
            useCase.getFund(ticker).body()?.let { emit(it) }
        }
    }

    fun getExchangeRate(dateReq: String): LiveData<Double> {
        return liveData {
            useCase.getCurrencies(dateReq).body()?.let {
                val rate = it.Valute[13].Value.replace(",", ".").toDouble()
                emit(rate)
            }
        }
    }
}