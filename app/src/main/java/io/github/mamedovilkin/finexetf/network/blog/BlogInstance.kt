package io.github.mamedovilkin.finexetf.network.blog

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.github.mamedovilkin.finexetf.BuildConfig

class BlogInstance {

    companion object {

        fun getInstance(): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(BuildConfig.BLOG_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}