package io.github.mamedovilkin.finexetf.di

import dagger.hilt.components.SingletonComponent
import io.github.mamedovilkin.finexetf.network.cbr.CBRInstance
import io.github.mamedovilkin.finexetf.network.cbr.CBRService
import io.github.mamedovilkin.finexetf.network.finex.FinExInstance
import io.github.mamedovilkin.finexetf.network.finex.FinExService
import io.github.mamedovilkin.finexetf.network.blog.BlogService
import io.github.mamedovilkin.finexetf.network.blog.BlogInstance
import retrofit2.Retrofit
import dagger.Module
import dagger.Provides
import javax.inject.Named
import dagger.hilt.InstallIn
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