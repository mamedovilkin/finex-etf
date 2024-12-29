package io.github.mamedovilkin.finexetf.repository

import io.github.mamedovilkin.finexetf.model.Fond
import io.github.mamedovilkin.finexetf.model.Fonds
import io.github.mamedovilkin.finexetf.retrofit.Service
import retrofit2.Response
import javax.inject.Inject

class FondRemoteRepository @Inject constructor(private val service: Service) {

    suspend fun getFonds(): Response<Fonds> {
        return service.getFonds()
    }

    suspend fun getFondByTicker(ticker: String): Response<Fond> {
        return service.getFondByTicker(ticker)
    }
}