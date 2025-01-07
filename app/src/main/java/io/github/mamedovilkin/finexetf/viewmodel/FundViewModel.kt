package io.github.mamedovilkin.finexetf.viewmodel

import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.model.network.finex.Fund
import io.github.mamedovilkin.finexetf.repository.UseCase
import java.util.Date
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

    fun getExchangeRate(): LiveData<List<Double>> {
        return liveData {
            val formattedDate = DateFormat.format("dd/MM/yyyy", Date()).toString()
            val currencyResponse = useCase.getCurrencies(formattedDate).body()

            if (currencyResponse == null) {
                emit(emptyList())
                return@liveData
            }

            val exchangeRateUSD = currencyResponse.Valute.getOrNull(13)?.Value?.replace(",", ".")?.toDoubleOrNull()
            val exchangeRateEUR = currencyResponse.Valute.getOrNull(14)?.Value?.replace(",", ".")?.toDoubleOrNull()
            val exchangeRateKZT = currencyResponse.Valute.getOrNull(18)?.VunitRate?.replace(",", ".")?.toDoubleOrNull()

            if (exchangeRateUSD == null ||
                exchangeRateEUR == null ||
                exchangeRateKZT == null) {
                emit(listOf(0.0, 0.0, 0.0))
                return@liveData
            }

            emit(listOf(exchangeRateUSD, exchangeRateEUR, exchangeRateKZT))
        }
    }
}