package io.github.mamedovilkin.finexetf.retrofit

import io.github.mamedovilkin.finexetf.model.Fund
import io.github.mamedovilkin.finexetf.model.Funds
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {
    @GET("v1/fonds/")
    suspend fun getFunds(): Response<Funds>

    @GET("v1/fonds/{ticker}")
    suspend fun getFundByTicker(@Path("ticker") ticker: String): Response<Fund>
}