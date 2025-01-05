package io.github.mamedovilkin.finexetf.di

import dagger.hilt.components.SingletonComponent
import io.github.mamedovilkin.finexetf.network.CBRInstance
import io.github.mamedovilkin.finexetf.network.CBRService
import io.github.mamedovilkin.finexetf.network.FinExInstance
import io.github.mamedovilkin.finexetf.network.FinExService
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
}