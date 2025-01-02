package io.github.mamedovilkin.finexetf.repository

import io.github.mamedovilkin.finexetf.model.Fund
import io.github.mamedovilkin.finexetf.model.Funds
import io.github.mamedovilkin.finexetf.retrofit.Service
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val service: Service) {

    suspend fun getFunds(): Response<Funds> {
        return service.getFunds()
    }

    suspend fun getFundByTicker(ticker: String): Response<Fund> {
        return service.getFundByTicker(ticker)
    }
}