package io.github.mamedovilkin.finexetf.network.finex

import io.github.mamedovilkin.finexetf.model.network.finex.Fund
import io.github.mamedovilkin.finexetf.model.network.finex.ListFund
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FinExService {
    @GET("v1/fonds/")
    suspend fun getFunds(): Response<List<ListFund>>

    @GET("v1/fonds/{ticker}")
    suspend fun getFund(@Path("ticker") ticker: String): Response<Fund>
}