package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.finexetf.room.Converter
import io.github.mamedovilkin.finexetf.room.Fund
import io.github.mamedovilkin.finexetf.room.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    fun insert(fund: Fund) {
        CoroutineScope(Dispatchers.IO).launch {
            useCase.insert(fund)
        }
    }

    fun getFundByTicker(ticker: String): LiveData<io.github.mamedovilkin.finexetf.model.Fund> {
        return liveData {
            useCase.getFundByTicker(ticker).body()?.let { emit(it) }
        }
    }

    fun getFundQuantityByTicker(ticker: String): LiveData<Int> {
        return liveData {
            useCase.getLocalFunds().asFlow().collect {
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