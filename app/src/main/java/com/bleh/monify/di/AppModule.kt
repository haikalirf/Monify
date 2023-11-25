package com.bleh.monify.di

import android.content.Context
import androidx.room.Room
import com.bleh.monify.core.daos.UserDao
import com.bleh.monify.core.database.AppDatabase
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
        @ApplicationContext
        context: Context,
    ): GoogleAuthClient {
        return GoogleAuthClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "room_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase) : UserDao {
        return appDatabase.userDao()
    }
}