package io.github.mamedovilkin.finexetf.network

import io.github.mamedovilkin.finexetf.model.network.Fund
import io.github.mamedovilkin.finexetf.model.network.ListFund
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FinExService {
    @GET("v1/fonds/")
    suspend fun getFunds(): Response<List<ListFund>>

    @GET("v1/fonds/{ticker}")
    suspend fun getFund(@Path("ticker") ticker: String): Response<Fund>
}