package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.model.Asset
import io.github.mamedovilkin.finexetf.model.Funds
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.finexetf.room.Converter
import io.github.mamedovilkin.finexetf.room.Type
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class MyAssetsViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    val remoteFunds: LiveData<Funds> = liveData { useCase.getFunds().body()?.let { emit(it) } }

    fun getAssets(): LiveData<List<Asset>> {
        return liveData {
            val assets = ArrayList<Asset>()

            useCase.getLocalFunds().asFlow().collect {
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
                        if (Converter.toType(it.type) == Type.PURCHASE) {
                            totalQuantity += it.quantity
                            totalPrice += (it.quantity * it.price)
                            totalNavPrice += (it.quantity * (it.navPrice * 111))
                        } else {
                            totalQuantity -= it.quantity
                            totalPrice -= (it.quantity * it.price)
                            totalNavPrice -= (it.quantity * (it.navPrice * 111))
                        }
                    }

                    assets.add(Asset(ticker, icon, name, originalName, navPrice, totalQuantity, totalPrice, totalNavPrice))
                }

                emit(assets)
            }
        }
    }
}