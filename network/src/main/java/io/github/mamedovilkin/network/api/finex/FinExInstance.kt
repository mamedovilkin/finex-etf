package io.github.mamedovilkin.network.api.finex

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.github.mamedovilkin.network.BuildConfig

class FinExInstance {

    companion object {

        fun getInstance(): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(BuildConfig.FINEX_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}