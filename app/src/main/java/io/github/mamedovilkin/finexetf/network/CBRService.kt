package io.github.mamedovilkin.finexetf.network

import io.github.mamedovilkin.finexetf.model.network.ValCurs
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CBRService {
    @Headers("Accept: application/xml")
    @GET("XML_daily.asp")
    suspend fun getCurrencies(@Query("date_req") dateReq: String): Response<ValCurs>
}