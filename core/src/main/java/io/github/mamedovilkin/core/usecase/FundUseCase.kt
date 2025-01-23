package io.github.mamedovilkin.core.usecase

import io.github.mamedovilkin.core.repository.NetworkRepository
import io.github.mamedovilkin.network.model.cbr.ValCurs
import io.github.mamedovilkin.network.model.finex.Fund
import retrofit2.Response
import javax.inject.Inject

class FundUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
) {

    suspend fun getFund(ticker: String): Response<Fund> {
        return networkRepository.getFund(ticker)
    }

    suspend fun getCurrencies(dateReq: String): Response<ValCurs> {
        return networkRepository.getCurrencies(dateReq)
    }
}