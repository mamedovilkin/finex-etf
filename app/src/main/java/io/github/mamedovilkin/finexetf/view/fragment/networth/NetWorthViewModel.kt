package io.github.mamedovilkin.finexetf.view.fragment.networth

import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.core.usecase.NetWorthUseCase
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.finexetf.view.adapter.myassets.model.Currency
import io.github.mamedovilkin.network.model.finex.Fund
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NetWorthViewModel @Inject constructor(
    private val netWorthUseCase: NetWorthUseCase
) : ViewModel() {

    fun getNetWorth(currency: Currency): LiveData<List<Double>> {
        return liveData {
            val formattedDate = DateFormat.format("dd/MM/yyyy", Date()).toString()
            val currencyResponse = netWorthUseCase.getCurrencies(formattedDate).body()

            if (currencyResponse == null) {
                emit(listOf(0.0, 0.0, 0.0))
                return@liveData
            }

            val exchangeRates = mapOf(
                "USD" to currencyResponse.Valute.getOrNull(13)?.Value?.replace(",", ".")?.toDoubleOrNull(),
                "EUR" to currencyResponse.Valute.getOrNull(14)?.Value?.replace(",", ".")?.toDoubleOrNull(),
                "KZT" to currencyResponse.Valute.getOrNull(18)?.VunitRate?.replace(",", ".")?.toDoubleOrNull()
            )

            if (exchangeRates.values.any { it == null }) {
                emit(listOf(0.0, 0.0, 0.0))
                return@liveData
            }

            val funds = mutableMapOf<String, Fund>()
            netWorthUseCase.assets.asFlow().collect { assets ->
                val groupedAssets = assets.groupBy { it.ticker }

                var totalNavNetWorth = 0.0
                var totalNetWorth = 0.0

                groupedAssets.forEach { (ticker, assetList) ->
                    val fund = funds.getOrPut(ticker) {
                        netWorthUseCase.getFund(ticker).body() ?: return@forEach
                    }

                    val navPerShare = fund.nav.navPerShare
                    val currencyNav = fund.nav.currencyNav
                    val conversionRate = when (currencyNav) {
                        "USD" -> exchangeRates["USD"] ?: 1.0
                        "EUR" -> exchangeRates["EUR"] ?: 1.0
                        "KZT" -> exchangeRates["KZT"] ?: 1.0
                        else -> 1.0
                    }

                    val navConversionRate = when (currency) {
                        Currency.USD -> conversionRate / (exchangeRates["USD"] ?: 1.0)
                        Currency.EUR -> conversionRate / (exchangeRates["EUR"] ?: 1.0)
                        Currency.KZT -> conversionRate / (exchangeRates["KZT"] ?: 1.0)
                        else -> conversionRate
                    }

                    assetList.forEach { asset ->
                        val multiplier = if (Converter.toType(asset.type) == Type.PURCHASE) 1 else -1
                        val navValue = navPerShare * navConversionRate
                        totalNavNetWorth += multiplier * asset.quantity * navValue

                        val priceConversionRate = when (currency) {
                            Currency.USD -> exchangeRates["USD"] ?: 1.0
                            Currency.EUR -> exchangeRates["EUR"] ?: 1.0
                            Currency.KZT -> exchangeRates["KZT"] ?: 1.0
                            else -> 1.0
                        }
                        totalNetWorth += multiplier * asset.quantity * (asset.price / priceConversionRate)
                    }
                }

                val change = totalNavNetWorth - totalNetWorth
                val percentChange = if (totalNavNetWorth != 0.0) (change * 100) / totalNavNetWorth else 0.0

                emit(listOf(totalNavNetWorth, change, percentChange))
            }
        }
    }
}