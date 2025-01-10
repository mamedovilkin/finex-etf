package io.github.mamedovilkin.finexetf.viewmodel.myassets

import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.finexetf.model.Currency
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NetWorthViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    fun getNetWorth(currency: Currency): LiveData<List<Double>> {
        return liveData {
            val formattedDate = DateFormat.format("dd/MM/yyyy", Date()).toString()
            val currencyResponse = useCase.getCurrencies(formattedDate).body()

            if (currencyResponse == null) {
                emit(listOf(0.0, 0.0, 0.0))
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

            var navNetWorth: Double
            var netWorth: Double

            useCase.getAssets().asFlow().collect { assets ->
                val grouping = assets.groupBy { it.ticker }

                val processedAssets = grouping.map { (_, assetList) ->
                    var localNavNetWorth = 0.0
                    var localNetWorth = 0.0

                    assetList.forEach { asset ->
                        val multiplier = if (Converter.toType(asset.type) == Type.PURCHASE) 1 else -1
                        val navValue = when (currency) {
                            Currency.USD -> {
                                when (asset.currencyNav) {
                                    "RUB" -> {
                                        asset.navPrice / exchangeRateUSD
                                    }
                                    "EUR" -> {
                                        (asset.navPrice * exchangeRateEUR) / exchangeRateUSD
                                    }
                                    "KZT" -> {
                                        (asset.navPrice * exchangeRateKZT) / exchangeRateUSD
                                    }
                                    else -> {
                                        asset.navPrice
                                    }
                                }
                            }
                            Currency.EUR -> {
                                when (asset.currencyNav) {
                                    "RUB" -> {
                                        asset.navPrice / exchangeRateEUR
                                    }
                                    "USD" -> {
                                        (asset.navPrice * exchangeRateUSD) / exchangeRateEUR
                                    }
                                    "KZT" -> {
                                        (asset.navPrice * exchangeRateKZT) / exchangeRateEUR
                                    }
                                    else -> {
                                        asset.navPrice
                                    }
                                }
                            }
                            Currency.KZT -> {
                                when (asset.currencyNav) {
                                    "RUB" -> {
                                        asset.navPrice / exchangeRateKZT
                                    }
                                    "USD" -> {
                                        (asset.navPrice * exchangeRateUSD) / exchangeRateKZT
                                    }
                                    "EUR" -> {
                                        (asset.navPrice * exchangeRateEUR) / exchangeRateKZT
                                    }
                                    else -> {
                                        asset.navPrice
                                    }
                                }
                            }
                            else -> {
                                when (asset.currencyNav) {
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
                            }
                        }

                        localNavNetWorth += multiplier * asset.quantity * navValue
                        localNetWorth += when (currency) {
                            Currency.USD -> {
                                multiplier * asset.quantity * (asset.price / exchangeRateUSD)
                            }
                            Currency.EUR -> {
                                multiplier * asset.quantity * (asset.price / exchangeRateEUR)
                            }
                            Currency.KZT -> {
                                multiplier * asset.quantity * (asset.price / exchangeRateKZT)
                            }
                            else -> {
                                multiplier * asset.quantity * asset.price
                            }
                        }
                    }

                    localNavNetWorth to localNetWorth
                }

                navNetWorth = processedAssets.sumOf { it.first }
                netWorth = processedAssets.sumOf { it.second }

                val change = navNetWorth - netWorth
                val percentChange = if (navNetWorth != 0.0) (change * 100) / navNetWorth else 0.0

                emit(listOf(navNetWorth, change, percentChange))
            }
        }
    }
}