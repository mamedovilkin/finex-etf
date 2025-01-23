package io.github.mamedovilkin.finexetf.view.fragment.myassets

import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.core.usecase.MyAssetsUseCase
import io.github.mamedovilkin.finexetf.view.adapter.myassets.model.Asset
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.network.model.finex.Fund
import io.github.mamedovilkin.network.model.finex.ListFund
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MyAssetsViewModel @Inject constructor(
    private val myAssetsUseCase: MyAssetsUseCase
) : ViewModel() {

    val funds: LiveData<List<ListFund>> = myAssetsUseCase.funds

    fun getExchangeRate(): LiveData<List<Double>> {
        return liveData {
            val formattedDate = DateFormat.format("dd/MM/yyyy", Date()).toString()
            val currencyResponse = myAssetsUseCase.getCurrencies(formattedDate).body()

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
            val currencyResponse = myAssetsUseCase.getCurrencies(formattedDate).body()

            if (currencyResponse == null) {
                emit(emptyList())
                return@liveData
            }

            val exchangeRates = mapOf(
                "USD" to currencyResponse.Valute.getOrNull(13)?.Value?.replace(",", ".")?.toDoubleOrNull(),
                "EUR" to currencyResponse.Valute.getOrNull(14)?.Value?.replace(",", ".")?.toDoubleOrNull(),
                "KZT" to currencyResponse.Valute.getOrNull(18)?.VunitRate?.replace(",", ".")?.toDoubleOrNull()
            )

            if (exchangeRates.values.any { it == null }) {
                emit(emptyList())
                return@liveData
            }

            val funds = mutableMapOf<String, Fund>()
            myAssetsUseCase.assets.asFlow().collect { assetsList ->
                val groupedAssets = assetsList.groupBy { it.ticker }

                val processedAssets = groupedAssets.map { (ticker, assetList) ->
                    val fund = funds.getOrPut(ticker) {
                        myAssetsUseCase.getFund(ticker).body() ?: return@map null
                    }

                    val navPerShare = fund.nav.navPerShare
                    val currencyNav = fund.nav.currencyNav

                    val navMultiplier = exchangeRates[currencyNav] ?: 1.0
                    val navValue = navPerShare * navMultiplier

                    var totalQuantity = 0L
                    var totalPrice = 0.0
                    var totalNavPrice = 0.0

                    val icon = assetList.firstOrNull()?.icon.orEmpty()
                    val name = assetList.firstOrNull()?.name.orEmpty()
                    val originalName = assetList.firstOrNull()?.originalName.orEmpty()
                    val isActive = assetList.any { it.isActive }

                    assetList.forEach { asset ->
                        val multiplier = if (Converter.toType(asset.type) == Type.PURCHASE) 1 else -1
                        totalQuantity += multiplier * asset.quantity
                        totalPrice += multiplier * (asset.quantity * asset.price)
                        totalNavPrice += multiplier * (asset.quantity * navValue)
                    }

                    if (totalQuantity > 0) {
                        Asset(ticker, icon, name, originalName, isActive, navValue, totalQuantity, totalPrice, totalNavPrice)
                    } else {
                        null
                    }
                }

                emit(processedAssets.filterNotNull())
            }
        }
    }
}