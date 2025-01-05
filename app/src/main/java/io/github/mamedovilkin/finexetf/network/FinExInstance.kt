package io.github.mamedovilkin.finexetf.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FinExInstance {

    companion object {

        private const val BASE_URL = "https://api.finex-etf.ru/"

        fun getInstance(): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}