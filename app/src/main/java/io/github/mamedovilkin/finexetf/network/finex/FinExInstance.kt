package io.github.mamedovilkin.finexetf.network.finex

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.github.mamedovilkin.finexetf.BuildConfig

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