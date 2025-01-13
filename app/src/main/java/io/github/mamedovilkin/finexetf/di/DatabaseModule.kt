package io.github.mamedovilkin.finexetf.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.mamedovilkin.network.dao.FundDao
import io.github.mamedovilkin.database.database.AssetDatabase
import io.github.mamedovilkin.network.database.FundDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAssetDatabase(@ApplicationContext context: Context): AssetDatabase {
        return AssetDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFundDatabase(@ApplicationContext context: Context): FundDatabase {
        return FundDatabase.getDatabase(context)
    }

    @Provides
    fun provideFundDao(database: FundDatabase): FundDao = database.getDao()
}