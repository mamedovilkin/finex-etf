package io.github.mamedovilkin.finexetf.network

import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class CBRInstance {

    companion object {

        private const val BASE_URL = "https://www.cbr.ru/scripts/"

        fun getInstance(): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
        }
    }
}