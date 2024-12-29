package io.github.mamedovilkin.finexetf.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import io.github.mamedovilkin.finexetf.repository.FondLocalRepository
import io.github.mamedovilkin.finexetf.repository.FondRemoteRepository
import io.github.mamedovilkin.finexetf.repository.FondRepository
import io.github.mamedovilkin.finexetf.repository.FondUseCase
import io.github.mamedovilkin.finexetf.retrofit.Service
import io.github.mamedovilkin.finexetf.room.FondDatabase

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFondRemoteRepository(service: Service): FondRemoteRepository {
        return FondRemoteRepository(service)
    }

    @Provides
    @Singleton
    fun provideFondLocalRepository(database: FondDatabase): FondLocalRepository {
        return FondLocalRepository(database)
    }

    @Provides
    @Singleton
    fun provideFondRepository(
        remoteRepository: FondRemoteRepository,
        localRepository: FondLocalRepository,
    ): FondRepository {
        return FondRepository(remoteRepository, localRepository)
    }

    @Provides
    @Singleton
    fun provideFondUseCase(repository: FondRepository): FondUseCase {
        return FondUseCase(repository)
    }
}