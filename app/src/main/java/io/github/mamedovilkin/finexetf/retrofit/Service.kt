package io.github.mamedovilkin.finexetf.retrofit

import io.github.mamedovilkin.finexetf.model.Fond
import io.github.mamedovilkin.finexetf.model.Fonds
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {
    @GET("v1/fonds/")
    suspend fun getFonds(): Response<Fonds>

    @GET("v1/fonds/{ticker}")
    suspend fun getFondByTicker(@Path("ticker") ticker: String): Response<Fond>
}