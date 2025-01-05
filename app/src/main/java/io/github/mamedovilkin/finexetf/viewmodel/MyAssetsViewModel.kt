package io.github.mamedovilkin.finexetf.viewmodel

import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.model.view.Asset
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.finexetf.database.Converter
import io.github.mamedovilkin.finexetf.model.database.Type
import io.github.mamedovilkin.finexetf.model.network.ListFund
import io.github.mamedovilkin.finexetf.model.view.ExchangeRate
import java.util.ArrayList
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MyAssetsViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    val funds: LiveData<List<ListFund>> = liveData { useCase.getFunds().body()?.let { emit(it) } }

    fun getExchangeRate(dateReq: String): LiveData<ExchangeRate> {
        return liveData {
            useCase.getCurrencies(dateReq).body()?.let {
                val rate = String.format("%.2f", it.Valute[13].Value.replace(",", ".").toDouble())
                val dateFrom = it.Date
                emit(ExchangeRate(rate, dateFrom))
            }
        }
    }

    fun getNetWorthRUB(): LiveData<List<Double>> {
        return liveData {
            useCase.getAssets().asFlow().collect {
                val grouping = it.groupBy { it.ticker }
                var navNetWorth = 0.0
                var netWorth = 0.0

                grouping.forEach {
                    it.value.forEach {
                        useCase.getCurrencies(DateFormat.format("dd/MM/yyyy", Date()).toString()).body()?.let { valcurs ->
                            if (Converter.toType(it.type) == Type.PURCHASE) {
                                navNetWorth += (it.quantity * (it.navPrice * valcurs.Valute[13].Value.replace(",", ".").toDouble()))
                                netWorth += (it.quantity * it.price)
                            } else {
                                navNetWorth -= (it.quantity * (it.navPrice * valcurs.Valute[13].Value.replace(",", ".").toDouble()))
                                netWorth -= (it.quantity * it.price)
                            }
                        }
                    }
                }

                val change = navNetWorth - netWorth
                val percentChange = (change * 100) / navNetWorth

                emit(listOf(navNetWorth, change, percentChange))
            }
        }
    }

    fun getNetWorthUSD(): LiveData<List<Double>> {
        return liveData {
            useCase.getAssets().asFlow().collect {
                val grouping = it.groupBy { it.ticker }
                var navNetWorth = 0.0
                var netWorth = 0.0

                grouping.forEach {
                    it.value.forEach {
                        useCase.getCurrencies(DateFormat.format("dd/MM/yyyy", Date()).toString()).body()?.let { valcurs ->
                            if (Converter.toType(it.type) == Type.PURCHASE) {
                                navNetWorth += (it.quantity * it.navPrice)
                                netWorth += (it.quantity * (it.price / valcurs.Valute[13].Value.replace(",", ".").toDouble()))
                            } else {
                                navNetWorth -= (it.quantity * it.navPrice)
                                netWorth -= (it.quantity * (it.price / valcurs.Valute[13].Value.replace(",", ".").toDouble()))
                            }
                        }
                    }
                }

                val change = navNetWorth - netWorth
                val percentChange = (change * 100) / navNetWorth

                emit(listOf(navNetWorth, change, percentChange))
            }
        }
    }

    fun getAssets(): LiveData<List<Asset>> {
        return liveData {
            val assets = ArrayList<Asset>()

            useCase.getAssets().asFlow().collect {
                val grouping = it.groupBy { it.ticker }

                grouping.forEach {
                    val ticker: String = it.key
                    var icon = ""
                    var name = ""
                    var originalName = ""
                    var navPrice = 0.0
                    var totalQuantity = 0
                    var totalPrice = 0.0
                    var totalNavPrice = 0.0

                    it.value.forEach {
                        icon = it.icon
                        name = it.name
                        originalName = it.originalName
                        navPrice = it.navPrice
                        useCase.getCurrencies(DateFormat.format("dd/MM/yyyy", Date()).toString()).body()?.let { valcurs ->
                            if (Converter.toType(it.type) == Type.PURCHASE) {
                                totalQuantity += it.quantity
                                totalPrice += (it.quantity * it.price)
                                totalNavPrice += (it.quantity * (it.navPrice * valcurs.Valute[13].Value.replace(",", ".").toDouble()))
                            } else {
                                totalQuantity -= it.quantity
                                totalPrice -= (it.quantity * it.price)
                                totalNavPrice -= (it.quantity * (it.navPrice * valcurs.Valute[13].Value.replace(",", ".").toDouble()))
                            }
                        }
                    }

                    assets.add(Asset(ticker, icon, name, originalName, navPrice, totalQuantity, totalPrice, totalNavPrice))
                }

                emit(assets.filterIndexed { _, asset ->
                    asset.totalQuantity != 0
                })
            }
        }
    }
}