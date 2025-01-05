package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.finexetf.database.Converter
import io.github.mamedovilkin.finexetf.model.database.Asset
import io.github.mamedovilkin.finexetf.model.database.Type
import io.github.mamedovilkin.finexetf.model.network.Fund
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    fun insert(asset: Asset) {
        viewModelScope.launch {
            if (Converter.toType(asset.type) == Type.PURCHASE) {
                val firstSplit = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse("07/12/2018 00:00").time
                val secondSplit = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse("09/09/2021 00:00").time
                val thirdSplit = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse("07/10/2021 00:00").time
                val fourthSplit = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse("03/02/2022 00:00").time

                if (asset.ticker == "FXGD" && asset.datetime < fourthSplit) {
                    asset.quantity *= 10
                    asset.price /= 10
                }

                if (asset.ticker == "FXTB" && asset.datetime < fourthSplit) {
                    asset.quantity *= 10
                    asset.price /= 10
                }

                if (asset.ticker == "FXUS" && asset.datetime < thirdSplit) {
                    asset.quantity *= 100
                    asset.price /= 100
                }

                if (asset.ticker == "FXRL" && asset.datetime < thirdSplit) {
                    asset.quantity *= 100
                    asset.price /= 100
                }

                if (asset.ticker == "FXRB" && asset.datetime < thirdSplit) {
                    asset.quantity *= 100
                    asset.price /= 100
                }

                if (asset.ticker == "FXDE" && asset.datetime < secondSplit) {
                    asset.quantity *= 100
                    asset.price /= 100
                }

                if (asset.ticker == "FXRU" && asset.datetime < firstSplit) {
                    asset.quantity *= 10
                    asset.price /= 10
                } else if (asset.ticker == "FXRU" && asset.datetime > firstSplit && asset.datetime < fourthSplit) {
                    asset.quantity *= 10
                    asset.price /= 10
                }
            }

            useCase.insert(asset)
        }
    }

    fun getFundByTicker(ticker: String): LiveData<Fund> {
        return liveData {
            useCase.getFund(ticker).body()?.let { emit(it) }
        }
    }

    fun getFundQuantity(ticker: String): LiveData<Int> {
        return liveData {
            useCase.getAssets().asFlow().collect {
                val grouping = it.groupBy { it.ticker }
                var totalQuantity = 0

                grouping.forEach {
                    val t = it.key

                    it.value.forEach {
                        if (ticker == t) {
                            if (Converter.toType(it.type) == Type.PURCHASE) {
                                totalQuantity += it.quantity
                            } else {
                                totalQuantity -= it.quantity
                            }
                        }
                    }
                }

                emit(totalQuantity)
            }
        }
    }
}