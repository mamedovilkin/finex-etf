package io.github.mamedovilkin.finexetf.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import io.github.mamedovilkin.finexetf.repository.LocalRepository
import io.github.mamedovilkin.finexetf.repository.RemoteRepository
import io.github.mamedovilkin.finexetf.repository.Repository
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.finexetf.retrofit.Service
import io.github.mamedovilkin.finexetf.room.FundDatabase

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(service: Service): RemoteRepository {
        return RemoteRepository(service)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(database: FundDatabase): LocalRepository {
        return LocalRepository(database)
    }

    @Provides
    @Singleton
    fun provideRepository(
        remoteRepository: RemoteRepository,
        localRepository: LocalRepository,
    ): Repository {
        return Repository(remoteRepository, localRepository)
    }

    @Provides
    @Singleton
    fun provideUseCase(repository: Repository): UseCase {
        return UseCase(repository)
    }
}