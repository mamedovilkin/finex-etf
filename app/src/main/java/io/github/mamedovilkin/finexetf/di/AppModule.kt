package io.github.mamedovilkin.finexetf.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import io.github.mamedovilkin.finexetf.repository.DatabaseRepository
import io.github.mamedovilkin.finexetf.repository.NetworkRepository
import io.github.mamedovilkin.finexetf.repository.Repository
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.finexetf.network.CBRService
import io.github.mamedovilkin.finexetf.network.FinExService
import io.github.mamedovilkin.finexetf.database.AssetDatabase
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(
        @Named("FinEx") finExService: FinExService,
        @Named("CBR") cbrService: CBRService,
    ): NetworkRepository {
        return NetworkRepository(finExService, cbrService)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(database: AssetDatabase): DatabaseRepository {
        return DatabaseRepository(database)
    }

    @Provides
    @Singleton
    fun provideRepository(
        networkRepository: NetworkRepository,
        databaseRepository: DatabaseRepository,
    ): Repository {
        return Repository(networkRepository, databaseRepository)
    }

    @Provides
    @Singleton
    fun provideUseCase(repository: Repository): UseCase {
        return UseCase(repository)
    }
}