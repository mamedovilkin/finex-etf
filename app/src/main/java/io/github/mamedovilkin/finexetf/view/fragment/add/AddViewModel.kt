package io.github.mamedovilkin.finexetf.view.fragment.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.core.usecase.AddUseCase
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.database.util.Type
import io.github.mamedovilkin.network.model.finex.Fund
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addUseCase: AddUseCase
) : ViewModel() {

    fun insert(asset: Asset) {
        viewModelScope.launch {
            if (Converter.toType(asset.type) == Type.PURCHASE) {
                val firstSplit = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse("07/12/2018 00:00")?.time
                val secondSplit = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse("09/09/2021 00:00")?.time
                val thirdSplit = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse("07/10/2021 00:00")?.time
                val fourthSplit = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT).parse("03/02/2022 00:00")?.time

                if (fourthSplit != null && asset.ticker == "FXGD" && asset.datetime < fourthSplit) {
                    asset.quantity *= 10
                    asset.price /= 10
                }

                if (fourthSplit != null && asset.ticker == "FXTB" && asset.datetime < fourthSplit) {
                    asset.quantity *= 10
                    asset.price /= 10
                }

                if (thirdSplit != null &&  asset.ticker == "FXUS" && asset.datetime < thirdSplit) {
                    asset.quantity *= 100
                    asset.price /= 100
                }

                if (thirdSplit != null && asset.ticker == "FXRL" && asset.datetime < thirdSplit) {
                    asset.quantity *= 100
                    asset.price /= 100
                }

                if (thirdSplit != null && asset.ticker == "FXRB" && asset.datetime < thirdSplit) {
                    asset.quantity *= 100
                    asset.price /= 100
                }

                if (secondSplit != null && asset.ticker == "FXDE" && asset.datetime < secondSplit) {
                    asset.quantity *= 100
                    asset.price /= 100
                }

                if (firstSplit != null && asset.ticker == "FXRU" && asset.datetime < firstSplit) {
                    asset.quantity *= 10
                    asset.price /= 10
                } else if (firstSplit != null && fourthSplit != null && asset.ticker == "FXRU" && asset.datetime > firstSplit && asset.datetime < fourthSplit) {
                    asset.quantity *= 10
                    asset.price /= 10
                }
            }

            addUseCase.insert(asset)
        }
    }

    fun getFund(ticker: String): LiveData<Fund> {
        return liveData {
            addUseCase.getFund(ticker).body()?.let { emit(it) }
        }
    }

    fun getFundQuantity(ticker: String): LiveData<Long> {
        return liveData {
            addUseCase.assets.asFlow().collect {
                val grouping = it.groupBy { asset -> asset.ticker }
                var totalQuantity: Long = 0

                grouping.forEach { group ->
                    val t = group.key

                    group.value.forEach { asset ->
                        if (ticker == t) {
                            if (Converter.toType(asset.type) == Type.PURCHASE) {
                                totalQuantity += asset.quantity
                            } else {
                                totalQuantity -= asset.quantity
                            }
                        }
                    }
                }

                emit(totalQuantity)
            }
        }
    }
}