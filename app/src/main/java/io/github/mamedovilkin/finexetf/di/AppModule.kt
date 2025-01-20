package io.github.mamedovilkin.finexetf.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import io.github.mamedovilkin.network.dao.FundDao
import io.github.mamedovilkin.core.repository.CoreRepository
import io.github.mamedovilkin.database.database.AssetDatabase
import io.github.mamedovilkin.database.repository.DatabaseRepository
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.network.repository.NetworkRepository
import io.github.mamedovilkin.finexetf.repository.Repository
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.network.api.blog.BlogInstance
import io.github.mamedovilkin.network.api.blog.BlogService
import io.github.mamedovilkin.network.api.cbr.CBRInstance
import io.github.mamedovilkin.network.api.cbr.CBRService
import io.github.mamedovilkin.network.api.finex.FinExInstance
import io.github.mamedovilkin.network.api.finex.FinExService
import io.github.mamedovilkin.network.database.FundDatabase
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        @Named("FinEx") finExService: FinExService,
        @Named("CBR") cbrService: CBRService,
        @Named("Blog") blogService: BlogService,
        fundDao: FundDao,
    ): NetworkRepository {
        return NetworkRepository(finExService, cbrService, blogService, fundDao)
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(database: AssetDatabase): DatabaseRepository {
        return DatabaseRepository(database)
    }

    @Provides
    @Singleton
    fun provideCoreRepository(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore, assetDatabase: AssetDatabase): CoreRepository {
        return CoreRepository(firebaseAuth, firebaseFirestore, assetDatabase)
    }

    @Provides
    @Singleton
    fun provideRepository(
        networkRepository: NetworkRepository,
        databaseRepository: DatabaseRepository,
        coreRepository: CoreRepository,
    ): Repository {
        return Repository(networkRepository, databaseRepository, coreRepository)
    }

    @Provides
    @Singleton
    fun provideUseCase(repository: Repository): UseCase {
        return UseCase(repository)
    }
}