package io.github.mamedovilkin.finexetf.viewmodel.myassets

import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.model.Asset
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.network.model.finex.ListFund
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MyAssetsViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    val funds: LiveData<List<ListFund>> = liveData { useCase.getFunds().body()?.let { emit(it) } }

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

    fun getAssets(): LiveData<List<Asset>> {
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
                emit(emptyList())
                return@liveData
            }

            useCase.getAssets().asFlow().collect { assetsList ->
                val grouping = assetsList.groupBy { it.ticker }

                val processedAssets = grouping.map { (ticker, assetList) ->
                    var icon = ""
                    var name = ""
                    var originalName = ""
                    var navPrice = 0.0
                    var totalQuantity: Long = 0
                    var totalPrice = 0.0
                    var totalNavPrice = 0.0

                    assetList.forEach { asset ->
                        if (icon.isEmpty()) icon = asset.icon
                        if (name.isEmpty()) name = asset.name
                        if (originalName.isEmpty()) originalName = asset.originalName
                        val navValue = when (asset.currencyNav) {
                            "USD" -> {
                                asset.navPrice * exchangeRateUSD
                            }
                            "EUR" -> {
                                asset.navPrice * exchangeRateEUR
                            }
                            "KZT" -> {
                                asset.navPrice * exchangeRateKZT
                            }
                            else -> {
                                asset.navPrice
                            }
                        }

                        val multiplier = if (Converter.toType(asset.type) == Type.PURCHASE) 1 else -1
                        totalQuantity += multiplier * asset.quantity
                        totalPrice += multiplier * (asset.quantity * asset.price)
                        totalNavPrice += multiplier * (asset.quantity * navValue)
                    }

                    if (totalQuantity != 0L) {
                        Asset(ticker, icon, name, originalName, navPrice, totalQuantity, totalPrice, totalNavPrice)
                    } else {
                        null
                    }
                }

                emit(processedAssets.filterNotNull())
            }
        }
    }
}