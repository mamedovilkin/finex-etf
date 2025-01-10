package io.github.mamedovilkin.finexetf.di

import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import dagger.Module
import dagger.Provides
import javax.inject.Named
import dagger.hilt.InstallIn
import io.github.mamedovilkin.network.api.blog.BlogInstance
import io.github.mamedovilkin.network.api.blog.BlogService
import io.github.mamedovilkin.network.api.cbr.CBRInstance
import io.github.mamedovilkin.network.api.cbr.CBRService
import io.github.mamedovilkin.network.api.finex.FinExInstance
import io.github.mamedovilkin.network.api.finex.FinExService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("FinEx")
    fun provideFinExInstance(): Retrofit {
        return FinExInstance.getInstance()
    }

    @Provides
    @Singleton
    @Named("FinEx")
    fun provideFinExService(@Named("FinEx") retrofit: Retrofit): FinExService {
        return retrofit.create(FinExService::class.java)
    }

    @Provides
    @Singleton
    @Named("CBR")
    fun provideCBRInstance(): Retrofit {
        return CBRInstance.getInstance()
    }

    @Provides
    @Singleton
    @Named("CBR")
    fun provideCBRService(@Named("CBR") retrofit: Retrofit): CBRService {
        return retrofit.create(CBRService::class.java)
    }

    @Provides
    @Singleton
    @Named("Blog")
    fun provideBlogInstance(): Retrofit {
        return BlogInstance.getInstance()
    }

    @Provides
    @Singleton
    @Named("Blog")
    fun provideBlogService(@Named("Blog") retrofit: Retrofit): BlogService {
        return retrofit.create(BlogService::class.java)
    }
}