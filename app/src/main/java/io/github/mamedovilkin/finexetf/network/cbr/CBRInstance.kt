package io.github.mamedovilkin.finexetf.network.cbr

import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import io.github.mamedovilkin.finexetf.BuildConfig

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