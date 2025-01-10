package io.github.mamedovilkin.network.network.cbr

import io.github.mamedovilkin.network.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class CBRInstance {

    companion object {

        fun getInstance(): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(BuildConfig.CBR_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
        }
    }
}