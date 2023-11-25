package com.bleh.monify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bleh.monify.core.daos.UserDao
import com.bleh.monify.core.models.Budget
import com.bleh.monify.core.models.Category
import com.bleh.monify.core.models.Transaction
import com.bleh.monify.core.models.User
import com.bleh.monify.core.models.Wallet

@Database(
    entities = [
        User::class,
        Category::class,
        Wallet::class,
        Transaction::class,
        Budget::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}