package com.bleh.monify.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bleh.monify.core.daos.BudgetDao
import com.bleh.monify.core.daos.CategoryDao
import com.bleh.monify.core.daos.TransactionDao
import com.bleh.monify.core.daos.UserDao
import com.bleh.monify.core.daos.WalletDao
import com.bleh.monify.core.daos.BaseDao
import com.bleh.monify.core.database.AppDatabase
import com.bleh.monify.core.helper.ioThread
import com.bleh.monify.feature_auth.GoogleAuthClient
import com.google.android.gms.auth.api.identity.Identity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthUiClient(
        @ApplicationContext context: Context,
    ): GoogleAuthClient {
        return GoogleAuthClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "room_database"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideBaseDao(appDatabase: AppDatabase) : BaseDao {
        return appDatabase.baseDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase) : UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(appDatabase: AppDatabase) : TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: AppDatabase) : CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideWalletDao(appDatabase: AppDatabase) : WalletDao {
        return appDatabase.walletDao()
    }

    @Provides
    @Singleton
    fun provideBudgetDao(appDatabase: AppDatabase) : BudgetDao {
        return appDatabase.budgetDao()
    }
}